package net.decodex.loghub.backend.controllers;

import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.services.CreateOrganizationRequestDto;
import net.decodex.loghub.backend.services.OrganizationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "Get All Organizations As Pageable with predicate")
    @GetMapping("/pageable")
    @PreAuthorize("hasAuthority('ORGANIZATION:READ')")
    public Page<OrganizationDto> findAllPageable(@PageableDefault Pageable pageable, @QuerydslPredicate(root = Organization.class) Predicate predicate) {
        return organizationService.findAllPageable(predicate, pageable);
    }

    @GetMapping("")
    @Operation(summary = "Get Logged user Organization")
    public OrganizationDto findLoggedUserOrganization(Principal principal) {
        return organizationService.findLoggedUserOrganization(principal);
    }

    @GetMapping("/teams")
    @Operation(summary = "Get Logged user organization Teams")
    public List<TeamDto> findLoggedUserOrganizationTeams(Principal principal) {
        return organizationService.findLoggedUserOrganizationTeams(principal);
    }

    @GetMapping("/members")
    @Operation(summary = "Get Logged user organization Members")
    public List<UserDto> findLoggedUserOrganizationMembers(Principal principal) {
        return organizationService.findLoggedUserOrganizationMembers(principal);
    }

    @GetMapping("/projects")
    @Operation(summary = "Get Logged user organization Projects")
    public List<ProjectDto> findLoggedUserOrganizationProjects(Principal principal) {
        return organizationService.findLoggedUserOrganizationProjects(principal);
    }

    @GetMapping("/invitations")
    @Operation(summary = "Get Logged user organization Invitations")
    public List<InvitationDto> findLoggedUserOrganizationInvitations(Principal principal) {
        return organizationService.findLoggedUserOrganizationInvitations(principal);
    }

    @GetMapping("/slug/taken")
    @Operation(summary = "Retrieves information whether organization slug is taken or not")
    public boolean isOrganizationSlugTaken(@RequestParam String slug) {
        return organizationService.isOrganizationSlugTaken(slug);
    }

    @GetMapping("/{organizationId}")
    @Operation(summary = "Get Organization By Id")
    @PreAuthorize("hasAuthority('ORGANIZATION:READ')")
    public OrganizationDto findById(@PathVariable("organizationId") String organizationId) {
        return organizationService.findById(organizationId);
    }


    @Operation(summary = "Create Organization")
    @PostMapping("")
    @PreAuthorize("hasAuthority('ORGANIZATION:CREATE')")
    public OrganizationDto createOrganization(@RequestBody @Valid CreateOrganizationRequestDto dto, Principal principal) {
        return organizationService.createOrganization(dto, principal);
    }

}
