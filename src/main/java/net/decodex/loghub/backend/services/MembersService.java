package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.InvitationDto;
import net.decodex.loghub.backend.domain.dto.MembersDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.domain.dto.queries.InvitedSelector;
import net.decodex.loghub.backend.domain.dto.queries.MembersQueryDto;
import net.decodex.loghub.backend.domain.mappers.InvitationMapper;
import net.decodex.loghub.backend.domain.mappers.UserMapper;
import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.enums.InvitationStatus;
import net.decodex.loghub.backend.exceptions.specifications.BadRequestException;
import net.decodex.loghub.backend.exceptions.specifications.ForbiddenActionException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.InvitationRepository;
import net.decodex.loghub.backend.repositories.OrganizationRepository;
import net.decodex.loghub.backend.repositories.TeamRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembersService {

    private final AuthenticationService authenticationService;

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;

    public MembersDto findAll(Principal principal, MembersQueryDto query) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        if (query != null && query.getInvited() == InvitedSelector.TRUE) {
            var invitations = filterInvitations(query.getSearch(), query.getRoleIds(), user.getOrganization());
            return new MembersDto(Collections.emptyList(), invitations, null);
        } else if (query != null && query.getInvited() == InvitedSelector.FALSE) {
            var members = filterUsers(query.getSearch(), query.getRoleIds(), user.getOrganization());
            var owner = userMapper.toDto(user.getOrganization().getOwner());
            members.sort(Comparator.comparing(UserDto::getCreatedAt));
            return new MembersDto(members, Collections.emptyList(), owner);
        } else {
            String search = null;
            List<String> roleIds = null;
            if (query != null) {
                search = query.getSearch();
                roleIds = query.getRoleIds();
            }
            var users = filterUsers(search, roleIds, user.getOrganization());
            var invitations = filterInvitations(search, roleIds, user.getOrganization());
            var owner = userMapper.toDto(user.getOrganization().getOwner());
            users.sort(Comparator.comparing(UserDto::getCreatedAt));
            return new MembersDto(users, invitations, owner);
        }
    }

    private List<UserDto> filterUsers(String search, List<String> roleIds, Organization organization) {
        if (search == null && (roleIds == null || roleIds.isEmpty())) {
            var users = userRepository.findByOrganization(organization);
            return users.stream().map(userMapper::toDto).collect(Collectors.toList());
        } else if (search != null && roleIds != null && !roleIds.isEmpty()) {
            var users = userRepository.findByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrEmailIgnoreCaseAndRole_RoleIdInAndOrganization(search, search, search, roleIds, organization);
            return users.stream().map(userMapper::toDto).collect(Collectors.toList());
        } else if (search != null) {
            var users = userRepository.findByFirstNameContainsIgnoreCaseOrLastNameContainsIgnoreCaseOrEmailContainsIgnoreCaseAndOrganization(search, search, search, organization);
            return users.stream().map(userMapper::toDto).collect(Collectors.toList());
        } else {
            var users = userRepository.findByRole_RoleIdInAndOrganization(roleIds, organization);
            return users.stream().map(userMapper::toDto).collect(Collectors.toList());
        }
    }

    private List<InvitationDto> filterInvitations(String search, List<String> roleIds, Organization organization) {
        if (search == null && (roleIds == null || roleIds.isEmpty())) {
            var invitations = invitationRepository.findByOrganizationAndStatus(organization, InvitationStatus.INVITED);
            return invitations.stream().map(invitationMapper::toDto).collect(Collectors.toList());
        } else if (search != null && roleIds != null && !roleIds.isEmpty()) {
            var invitations = invitationRepository.findByEmailContainsIgnoreCaseAndRole_RoleIdInAndOrganizationAndStatus(search, roleIds, organization, InvitationStatus.INVITED);
            return invitations.stream().map(invitationMapper::toDto).collect(Collectors.toList());
        } else if (search != null) {
            var invitations = invitationRepository.findByEmailContainsIgnoreCaseAndOrganizationAndStatus(search, organization, InvitationStatus.INVITED);
            return invitations.stream().map(invitationMapper::toDto).collect(Collectors.toList());
        } else {
            var invitations = invitationRepository.findByRole_RoleIdInAndOrganizationAndStatus(roleIds, organization, InvitationStatus.INVITED);
            return invitations.stream().map(invitationMapper::toDto).collect(Collectors.toList());
        }
    }

    public Object deleteMember(String id, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        if (user.getOrganization().getOwner().getUserId().equals(id)) {
            throw new BadRequestException("You can't delete creator of the organization");
        }
        var organization = user.getOrganization();

        var userOp = this.userRepository.findById(id);
        var invitationOp = this.invitationRepository.findById(id);
        if (userOp.isPresent()) {
            var userToDelete = userOp.get();
            if (userToDelete.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
                organization.getMembers().remove(userToDelete);
                organizationRepository.save(organization);
                var teams = teamRepository.findByMembers(userToDelete);
                teams.forEach(team -> team.getMembers().remove(userToDelete));
                teamRepository.saveAll(teams);
                userRepository.delete(userToDelete);
                return ResponseEntity.noContent().build();
            } else {
                throw new ForbiddenActionException("Not member of organization");
            }
        } else if (invitationOp.isPresent()) {
            var invitationToDelete = invitationOp.get();
            if (invitationToDelete.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
                organization.getInvitations().remove(invitationToDelete);
                organizationRepository.save(organization);
                invitationRepository.delete(invitationToDelete);
                return ResponseEntity.noContent().build();
            } else {
                throw new ForbiddenActionException("Not member of organization");
            }
        } else {
            throw new ResourceNotFoundException("User", "userId", id);
        }
    }
}
