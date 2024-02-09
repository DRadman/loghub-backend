package net.decodex.loghub.backend.services;

import com.jlefebure.spring.boot.minio.MinioException;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.domain.mappers.*;
import net.decodex.loghub.backend.enums.InvitationStatus;
import net.decodex.loghub.backend.enums.ResourceType;
import net.decodex.loghub.backend.exceptions.specifications.BadRequestException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceAlreadyExistsException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.InvitationRepository;
import net.decodex.loghub.backend.repositories.OrganizationRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final TeamMapper teamMapper;
    private final InvitationMapper invitationMapper;
    private final InvitationRepository invitationRepository;
    private final FileStorageService fileStorageService;

    public OrganizationDto findById(String organizationId) {
        var organization = organizationRepository.findById(organizationId);
        if (organization.isPresent()) {
            return organizationMapper.toDto(organization.get());
        } else {
            throw new ResourceNotFoundException("Organization", "organizationId", organizationId);
        }
    }

    public Page<OrganizationDto> findAllPageable(Predicate predicate, Pageable pageable) {
        return this.organizationRepository.findAll(predicate, pageable).map(organizationMapper::toDto);
    }

    public OrganizationDto findLoggedUserOrganization(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new ResourceNotFoundException("You are not assigned to any organization");
        } else {
            return organizationMapper.toDto(user.getOrganization());
        }
    }

    public OrganizationDto createOrganization(CreateOrganizationRequestDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() != null) {
            throw new BadRequestException("You already have one organization");
        }
        if (organizationRepository.findBySlug(dto.getSlug()).isPresent()) {
            throw new ResourceAlreadyExistsException("Organization", "slug", dto.getSlug());
        }

        var organization = organizationMapper.toEntity(dto);
        if (!user.getRole().isInternal()) {
            organization.setOwner(user);
            organization.getMembers().add(user);
            organization = this.organizationRepository.save(organization);
            user.setOrganization(organization);
            this.userRepository.save(user);
        } else {
            organization = this.organizationRepository.save(organization);
        }

        return organizationMapper.toDto(organization);
    }

    public boolean isOrganizationSlugTaken(String slug) {
        return organizationRepository.findBySlug(slug).isPresent();
    }

    public List<UserDto> findLoggedUserOrganizationMembers(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        var organization = user.getOrganization();
        if (organization == null) {
            throw new OrganizationNotPresentException();
        }
        return organization.getMembers().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    public List<ProjectDto> findLoggedUserOrganizationProjects(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        var organization = user.getOrganization();
        if (organization == null) {
            throw new OrganizationNotPresentException();
        }
        return organization.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList());
    }

    public List<TeamDto> findLoggedUserOrganizationTeams(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        var organization = user.getOrganization();
        if (organization == null) {
            throw new OrganizationNotPresentException();
        }
        return organization.getTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
    }

    public List<InvitationDto> findLoggedUserOrganizationInvitations(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        var organization = user.getOrganization();
        if (organization == null) {
            throw new OrganizationNotPresentException();
        }

        return invitationRepository.findByOrganizationAndStatus(organization, InvitationStatus.INVITED).stream()
                .map(invitationMapper::toDto).collect(Collectors.toList());
    }

    @SneakyThrows
    public FileDto updatePicture(MultipartFile file, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        var organization = user.getOrganization();
        if (organization == null) {
            throw new OrganizationNotPresentException();
        }

        try {
            this.fileStorageService.deleteFile(organization.getPicture());
        } catch (NullPointerException e) {
            //Consume
        }

        var result = this.fileStorageService.addFile(ResourceType.ORGANIZATION + "_" + organization.getOrganizationId(), file);
        organization.setPicture(result.getFileName());
        organization.setPictureUrl(this.fileStorageService.getBasePublicUrl() + "/" + result.getFileName());
        result.setUrl(organization.getPictureUrl());

        this.organizationRepository.save(organization);

        return result;
    }
}
