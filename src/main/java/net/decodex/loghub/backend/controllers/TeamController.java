package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.annotations.IsMongoId;
import net.decodex.loghub.backend.domain.dto.requests.TeamRequestDto;
import net.decodex.loghub.backend.domain.dto.TeamDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.services.TeamService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/team")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/slug/taken")
    @Operation(summary = "Retrieves information team slug is taken or not")
    @PreAuthorize("hasAuthority('TEAM:READ')")
    public boolean isTeamSlugTaken(@RequestParam String slug, Principal principal) {
        return teamService.isSlugTaken(slug, principal);
    }

    @Operation(summary = "Get Team Members")
    @GetMapping(path = "/{teamId}/members")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:READ')")
    public List<UserDto> getTeamMembers(Principal principal, @PathVariable("teamId") @IsMongoId String teamId) {
        return teamService.getTeamMembers(teamId, principal);
    }

    @Operation(summary = "Add Team Member")
    @PutMapping(path = "/{teamId}/members")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:UPDATE')")
    public List<UserDto> addTeamMembers(@PathVariable("teamId") @IsMongoId String teamId, @RequestParam("ids") List<String> ids, Principal principal) {
        return teamService.addTeamMembers(teamId, ids, principal);
    }

    @Operation(summary = "Remove Team Members")
    @DeleteMapping(path = "/{teamId}/members")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:UPDATE')")
    public List<UserDto> removeTeamMembers(@PathVariable("teamId") @IsMongoId String teamId, @RequestParam("ids") List<String> ids, Principal principal) {
        return teamService.removeTeamMembers(teamId, ids, principal);
    }

    @Operation(summary = "Create Team")
    @PostMapping(path = "")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:CREATE')")
    public TeamDto createTeam(@RequestBody @Valid TeamRequestDto dto, Principal principal) {
        return teamService.createTeam(dto, principal);
    }

    @Operation(summary = "Update Team")
    @PostMapping(path = "/{teamId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:UPDATE')")
    public TeamDto UpdateTeam(@RequestBody @Valid TeamRequestDto dto, @PathVariable("teamId") @IsMongoId String teamId, Principal principal) {
        return teamService.updateTeam(dto, teamId, principal);
    }

    @Operation(summary = "Delete Team")
    @DeleteMapping(path = "/{teamId}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasAuthority('TEAM:DELETE')")
    public Object deleteTeam(@PathVariable("teamId") @IsMongoId String teamId, Principal principal) {
        return teamService.deleteTeam(teamId, principal);
    }

}
