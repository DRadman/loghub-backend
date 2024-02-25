package net.decodex.loghub.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.annotations.IsMongoId;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.domain.dto.requests.CreateProjectReleaseDto;
import net.decodex.loghub.backend.domain.dto.requests.ProjectRequestDto;
import net.decodex.loghub.backend.domain.mappers.ProjectMapper;
import net.decodex.loghub.backend.enums.DebugFileType;
import net.decodex.loghub.backend.services.ProjectService;
import net.decodex.loghub.backend.services.ProjectStatsService;
import net.decodex.loghub.backend.services.crons.ProjectStatCron;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectStatsService projectStatsService;
    private final ProjectMapper projectMapper;

    @GetMapping("")
    @Operation(summary = "Retrieves all projects")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<ProjectDto> findAllProjects(@RequestParam(required = false) String search, Principal principal) {
        return projectService.findAllProjects(search, principal);
    }

    @GetMapping("my")
    @Operation(summary = "Retrieves my projects")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<ProjectDto> findMyProjects(@RequestParam(required = false) List<String> teamId, Principal principal) {
        return projectService.findMyProjects(teamId, principal);
    }

    @GetMapping("/name/taken")
    @Operation(summary = "Retrieves information whether project name is taken or not")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public boolean isProjectNameTake(@RequestParam String name, Principal principal) {
        return projectService.isProjectNameTaken(name, principal);
    }

    @GetMapping("/details")
    @Operation(summary = "Retrieves information of project by api key")
    public ProjectDto getProjectKeyDetails(@RequestHeader("X-API-KEY") String key) {
        return projectMapper.toDto(projectService.getProjectByKey(key));
    }

    @Operation(summary = "Get Project Key")
    @GetMapping(path = "/{projectId}/key")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public String getProjectKey(Principal principal, @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectKey(projectId, principal);
    }

    @Operation(summary = "Get Project Teams")
    @GetMapping(path = "/{projectId}/teams")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<TeamDto> getProjectTeams(Principal principal, @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectTeams(projectId, principal);
    }

    @Operation(summary = "Get Project Stats")
    @GetMapping(path = "/{projectId}/stats")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public GeneralProjectStatDto getProjectStats(Principal principal, @PathVariable("projectId") @IsMongoId String projectId) {
        return projectStatsService.getProjectStats(projectId, principal);
    }

    @Operation(summary = "Get Project By id")
    @GetMapping(path = "/{projectId}")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public ProjectDto getProjectById(Principal principal, @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectById(projectId, principal);
    }

    @Operation(summary = "Add Project Teams")
    @PutMapping(path = "/{projectId}/teams")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<TeamDto> addProjectTeams(@PathVariable("projectId") @IsMongoId String projectId,
                                         @RequestParam("ids") List<String> ids, Principal principal) {
        return projectService.addProjectTeams(projectId, ids, principal);
    }

    @Operation(summary = "Remove Project Teams")
    @DeleteMapping(path = "/{projectId}/teams")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<TeamDto> removeProjectTeams(@PathVariable("projectId") @IsMongoId String projectId,
                                            @RequestParam("ids") List<String> ids, Principal principal) {
        return projectService.removeProjectTeams(projectId, ids, principal);
    }

    @Operation(summary = "Get Project Tags")
    @GetMapping(path = "/{projectId}/tags")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<String> getProjectTags(Principal principal, @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectTags(projectId, principal);
    }

    @Operation(summary = "Add Project Tags")
    @PutMapping(path = "/{projectId}/tags")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<String> addProjectTags(@PathVariable("projectId") @IsMongoId String projectId,
                                       @RequestParam("tags") List<String> tags, Principal principal) {
        return projectService.addProjectTags(projectId, tags, principal);
    }

    @Operation(summary = "Remove Project Tags")
    @DeleteMapping(path = "/{projectId}/tags")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<String> removeProjectTags(@PathVariable("projectId") @IsMongoId String projectId,
                                          @RequestParam("tags") List<String> tags, Principal principal) {
        return projectService.removeProjectTags(projectId, tags, principal);
    }

    @Operation(summary = "Get Project Environments")
    @GetMapping(path = "/{projectId}/environments")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<String> getProjectEnvironments(Principal principal,
                                               @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectEnvironments(projectId, principal);
    }

    @Operation(summary = "Add Project Environments")
    @PutMapping(path = "/{projectId}/environments")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<String> addProjectEnvironments(@PathVariable("projectId") @IsMongoId String projectId,
                                               @RequestParam("environments") List<String> ids, Principal principal) {
        return projectService.addProjectEnvironments(projectId, ids, principal);
    }

    @Operation(summary = "Remove Project Environments")
    @DeleteMapping(path = "/{projectId}/environments")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<String> removeProjectEnvironments(@PathVariable("projectId") @IsMongoId String projectId,
                                                  @RequestParam("environments") List<String> ids, Principal principal) {
        return projectService.removeProjectEnvironments(projectId, ids, principal);
    }

    @Operation(summary = "Get Project Releases")
    @GetMapping(path = "/{projectId}/releases")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<ProjectReleaseDto> getProjectReleases(Principal principal,
                                                      @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectReleases(projectId, principal);
    }

    @Operation(summary = "Add Project Release")
    @PutMapping(path = "/{projectId}/releases")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<ProjectReleaseDto> addProjectReleases(@PathVariable("projectId") @IsMongoId String projectId,
                                                      @RequestBody @Valid CreateProjectReleaseDto dto,
                                                      Principal principal) {
        return projectService.addProjectReleases(projectId, dto, principal);
    }

    @Operation(summary = "Remove Project Releases")
    @DeleteMapping(path = "/{projectId}/releases")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<ProjectReleaseDto> removeProjectReleases(@PathVariable("projectId") @IsMongoId String projectId,
                                                         @RequestParam("ids") List<String> ids, Principal principal) {
        return projectService.removeProjectReleases(projectId, ids, principal);
    }

    @Operation(summary = "Get Project Debug Files")
    @GetMapping(path = "/{projectId}/files")
    @PreAuthorize("hasAuthority('PROJECT:READ')")
    public List<DebugFileDto> getProjectDebugFiles(Principal principal,
                                                   @PathVariable("projectId") @IsMongoId String projectId) {
        return projectService.getProjectDebugFiles(projectId, principal);
    }

    @Operation(summary = "Add Project Debug File")
    @PutMapping(path = "/{projectId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<DebugFileDto> addProjectDebugFile(@PathVariable("projectId") @IsMongoId String projectId,
                                                  @RequestPart("file") @NotNull MultipartFile file,
                                                  @RequestParam @NotNull DebugFileType type, Principal principal) {
        return projectService.addProjectDebugFile(projectId, file, type, principal);
    }

    @Operation(summary = "Remove Project Debug Files")
    @DeleteMapping(path = "/{projectId}/files")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public List<DebugFileDto> removeProjectDebugFiles(@PathVariable("projectId") @IsMongoId String projectId,
                                                      @RequestParam("ids") List<String> ids, Principal principal) {
        return projectService.removeProjectDebugFiles(projectId, ids, principal);
    }

    @Operation(summary = "Create Project")
    @PostMapping(path = "")
    @PreAuthorize("hasAuthority('PROJECT:CREATE')")
    public ProjectDto createProject(@RequestBody @Valid ProjectRequestDto dto, Principal principal) {
        return projectService.createProject(dto, principal);
    }

    @Operation(summary = "Update Project")
    @PostMapping(path = "/{projectId}")
    @PreAuthorize("hasAuthority('PROJECT:UPDATE')")
    public ProjectDto updateProject(@RequestBody @Valid ProjectRequestDto dto,
                                    @PathVariable("projectId") @IsMongoId String projectId, Principal principal) {
        return projectService.updateProject(dto, projectId, principal);
    }

    @Operation(summary = "Delete Project")
    @DeleteMapping(path = "/{projectId}")
    @PreAuthorize("hasAuthority('PROJECT:DELETE')")
    public Object deleteTeam(@PathVariable("projectId") @IsMongoId String projectId, Principal principal) {
        return projectService.deleteProject(projectId, principal);
    }
}
