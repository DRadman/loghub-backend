package net.decodex.loghub.backend.services;

import com.mailjet.client.errors.MailjetException;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.CreateInvitationDto;
import net.decodex.loghub.backend.domain.dto.InvitationDto;
import net.decodex.loghub.backend.domain.dto.requests.RegisterUserRequestDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.domain.mappers.InvitationMapper;
import net.decodex.loghub.backend.domain.mappers.UserMapper;
import net.decodex.loghub.backend.domain.models.Invitation;
import net.decodex.loghub.backend.enums.InvitationStatus;
import net.decodex.loghub.backend.exceptions.specifications.BadRequestException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceAlreadyExistsException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.InvitationRepository;
import net.decodex.loghub.backend.repositories.OrganizationRepository;
import net.decodex.loghub.backend.repositories.RoleRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthenticationService authenticationService;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    public InvitationDto findById(String invitationId) {
        var invitation = invitationRepository.findById(invitationId);
        if (invitation.isEmpty()) {
            throw new ResourceNotFoundException("Invitation", "invitationId", invitationId);
        }

        return invitationMapper.toDto(invitation.get());
    }


    public UserDto acceptInvitation(String invitationId, RegisterUserRequestDto userDto) {
        var invitationOp = invitationRepository.findById(invitationId);
        if (invitationOp.isEmpty()) {
            throw new ResourceNotFoundException("Invitation", "invitationId", invitationId);
        }

        if (invitationOp.get().getStatus() == InvitationStatus.ACCEPTED) {
            throw new BadRequestException("Invitation already accepted");
        }

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("User", "username", userDto.getUsername());
        }

        var invitation = invitationOp.get();
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);

        var organization = invitation.getOrganization();

        var user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActivated(true);
        user.setOrganization(organization);
        user.setRole(invitation.getRole());
        user = userRepository.save(user);

        organization.getMembers().add(user);
        organizationRepository.save(organization);

        return userMapper.toDto(user);
    }

    public InvitationDto sendInvitation(Principal principal, CreateInvitationDto dto) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        var role = roleRepository.findById(dto.getRoleId());
        if(role.isEmpty()) {
            throw new ResourceNotFoundException("Role", "roleId", dto.getRoleId());
        }

        var organization = user.getOrganization();

        var invitation = new Invitation();
        invitation.setStatus(InvitationStatus.INVITED);
        invitation.setEmail(dto.getEmail());
        invitation.setRole(role.get());
        invitation.setOrganization(organization);
        invitation = invitationRepository.save(invitation);

        organization.getInvitations().add(invitation);
        organizationRepository.save(organization);

        try {
            mailService.sendInvitationEmail(invitation);
        } catch (MailjetException e) {
            throw new RuntimeException(e);
        }

        return invitationMapper.toDto(invitation);
    }
}
