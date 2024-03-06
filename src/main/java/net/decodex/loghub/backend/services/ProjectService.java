package net.decodex.loghub.backend.services;

import com.jlefebure.spring.boot.minio.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.decodex.loghub.backend.domain.dto.*;
import net.decodex.loghub.backend.domain.dto.requests.CreateProjectReleaseDto;
import net.decodex.loghub.backend.domain.dto.requests.ProjectRequestDto;
import net.decodex.loghub.backend.domain.mappers.*;
import net.decodex.loghub.backend.domain.models.DebugFile;
import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.enums.DebugFileType;
import net.decodex.loghub.backend.enums.ResourceType;
import net.decodex.loghub.backend.exceptions.specifications.*;
import net.decodex.loghub.backend.repositories.*;
import net.decodex.loghub.backend.repositories.elastic.ProjectStatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final FileStorageService fileStorageService;
    private final AuthenticationService authenticationService;
    private final CryptoService cryptoService;

    private final LogSourceRepository logSourceRepository;
    private final LogEntryRepository logEntryRepository;
    private final LogSessionRepository logSessionRepository;
    private final ProjectReleaseRepository projectReleaseRepository;
    private final IssueRepository issueRepository;
    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;
    private final DebugFileRepository debugFileRepository;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    private final TeamMapper teamMapper;
    private final ProjectReleaseMapper projectReleaseMapper;
    private final DebugFileMapper debugFileMapper;
    private final PlatformRepository platformRepository;
    private final PlatformMapper platformMapper;
    private final ProjectStatRepository projectStatRepository;

    public List<ProjectDto> findAllProjects(String search, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        if (search == null || search.isEmpty()) {
            return user.getOrganization().getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList());
        } else {
            var projects = projectRepository.findByNameContainsIgnoreCaseAndOrganization(search, user.getOrganization());
            return projects.stream().map(projectMapper::toDto).collect(Collectors.toList());
        }
    }

    public List<ProjectDto> findMyProjects(List<String> teamIds, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        var projects = new ArrayList<ProjectDto>();
        if (teamIds == null || teamIds.isEmpty()) {
            user.getTeams().forEach(team -> projects.addAll(team.getProjects().stream().map(projectMapper::toDto).toList()));
        } else {
            user.getTeams().stream().filter(team -> teamIds.contains(team.getTeamId())).forEach(team ->
                    projects.addAll(team.getProjects().stream().map(projectMapper::toDto).toList()));
        }
        return projects;
    }

    public List<TeamDto> getProjectTeams(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectOp);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return project.getProjectTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<TeamDto> addProjectTeams(String projectId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var teamsToAdd = teamRepository.findAllById(ids);
            project.getProjectTeams().addAll(teamsToAdd);
            teamsToAdd.forEach(team -> team.getProjects().add(project));
            teamRepository.saveAll(teamsToAdd);
            projectRepository.save(project);

            return project.getProjectTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<TeamDto> removeProjectTeams(String projectId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var teamsToRemove = teamRepository.findAllById(ids);
            project.getProjectTeams().removeAll(teamsToRemove);
            teamsToRemove.forEach(team -> team.getProjects().remove(project));
            teamRepository.saveAll(teamsToRemove);
            projectRepository.save(project);

            return project.getProjectTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> getProjectTags(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectOp);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return project.getTags();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> addProjectTags(String projectId, List<String> tags, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            project.getTags().addAll(tags);
            projectRepository.save(project);

            return project.getTags();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> removeProjectTags(String projectId, List<String> tags, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            project.getTags().removeAll(tags);
            projectRepository.save(project);

            return project.getTags();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> getProjectEnvironments(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectOp);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return project.getEnvironments();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> addProjectEnvironments(String projectId, List<String> environments, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            project.getEnvironments().addAll(environments);
            projectRepository.save(project);

            return project.getEnvironments();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<String> removeProjectEnvironments(String projectId, List<String> environments, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            project.getEnvironments().removeAll(environments);
            projectRepository.save(project);

            return project.getTags();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public void purgeProjects(Collection<Project> projects) {
        logSourceRepository.deleteByProjectIn(projects);
        var entries = logEntryRepository.deleteByProjectIn(projects);
        logSessionRepository.deleteByProjectIn(projects);
        projectReleaseRepository.deleteByProjectIn(projects);

        var issues = issueRepository.findByLogEntriesIn(entries);
        issues.forEach(issue -> issue.getLogEntries().removeAll(entries));
        issueRepository.saveAll(issues);

        var organizations = organizationRepository.findByProjectsIn(projects);
        organizations.forEach(organization -> organization.getProjects().removeAll(projects));
        organizationRepository.saveAll(organizations);

        var debugFiles = debugFileRepository.deleteByProjectIn(projects);
        debugFiles.forEach(debugFile -> {
            try {
                fileStorageService.deleteFile(debugFile.getFile());
            } catch (MinioException e) {
                //Consume
            }
        });

        var teams = teamRepository.findByProjectsIn(projects);
        teams.forEach(team -> team.getProjects().removeAll(projects));
        teamRepository.saveAll(teams);
    }

    public boolean isProjectNameTaken(String name, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        return projectRepository.existsByNameAndOrganization(name, user.getOrganization());
    }

    public List<ProjectReleaseDto> getProjectReleases(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectOp);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return project.getReleases().stream().map(projectReleaseMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<ProjectReleaseDto> addProjectReleases(String projectId, CreateProjectReleaseDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        if (projectReleaseRepository.existsByVersionAndProject(dto.getVersion(), projectOp.get())) {
            throw new ResourceAlreadyExistsException("ProjectRelease", "version", dto.getVersion());
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var release = projectReleaseMapper.toEntity(dto);
            release.setProject(project);
            release = projectReleaseRepository.save(release);
            project.getReleases().add(release);
            projectRepository.save(project);

            return project.getReleases().stream().map(projectReleaseMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<ProjectReleaseDto> removeProjectReleases(String projectId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var releasesToRemove = projectReleaseRepository.findAllById(ids);
            project.getReleases().removeAll(releasesToRemove);
            projectReleaseRepository.deleteAll(releasesToRemove);
            projectRepository.save(project);

            return project.getReleases().stream().map(projectReleaseMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<DebugFileDto> getProjectDebugFiles(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectOp);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return project.getDebugFiles().stream().map(debugFileMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    @SneakyThrows
    public List<DebugFileDto> addProjectDebugFile(String projectId, MultipartFile file, DebugFileType type, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var debugFile = new DebugFile();
            debugFile.setType(type);
            debugFile.setProject(project);
            debugFile = debugFileRepository.save(debugFile);
            var result = this.fileStorageService.addFile(ResourceType.DEBUG_FILE + "_" + debugFile.getDebugFileId(), file);
            debugFile.setFile(result.getFileName());
            debugFile.setFileUrl(this.fileStorageService.getBasePrivateUrl() + "/" + result.getFileName());
            debugFileRepository.save(debugFile);
            project.getDebugFiles().add(debugFile);
            projectRepository.save(project);

            return project.getDebugFiles().stream().map(debugFileMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<DebugFileDto> removeProjectDebugFiles(String projectId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var filesToRemove = debugFileRepository.findAllById(ids);
            project.getDebugFiles().removeAll(filesToRemove);

            filesToRemove.forEach(debugFile -> {
                try {
                    fileStorageService.deleteFile(debugFile.getFile());
                } catch (MinioException e) {
                    //Consume
                }
            });

            debugFileRepository.deleteAll(filesToRemove);
            projectRepository.save(project);

            return project.getDebugFiles().stream().map(debugFileMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public Object deleteProject(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var deletedEntities = projectRepository.deleteByProjectId(projectId);
            purgeProjects(deletedEntities);

            return ResponseEntity.noContent().build();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public ProjectDto createProject(ProjectRequestDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        var platformOp = platformRepository.findById(dto.getPlatformId());
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", dto.getPlatformId());
        }

        if (projectRepository.existsByNameAndOrganization(dto.getName(), user.getOrganization())) {
            throw new ResourceAlreadyExistsException("Project", "name", dto.getName());
        }

        var project = new Project();
        var organization = user.getOrganization();
        project.setName(dto.getName());
        project.setPlatform(platformOp.get());
        project.setTags(platformOp.get().getDefaultTags());
        project.setOrganization(organization);
        project = projectRepository.save(project);
        organization.getProjects().add(project);
        organizationRepository.save(organization);
        return projectMapper.toDto(project);
    }

    public ProjectDto updateProject(ProjectRequestDto dto, String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var platformOp = platformRepository.findById(dto.getPlatformId());
        if (platformOp.isEmpty()) {
            throw new ResourceNotFoundException("Platform", "platformId", dto.getPlatformId());
        }

        if (projectRepository.existsByNameAndOrganization(dto.getName(), user.getOrganization())) {
            throw new ResourceAlreadyExistsException("Project", "name", dto.getName());
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            project.setName(dto.getName());
            project.setPlatform(platformOp.get());
            project.setTags(platformOp.get().getDefaultTags());
            project = projectRepository.save(project);
            return projectMapper.toDto(project);
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public ProjectClientSecurityDto getProjectSecurityClient(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return new ProjectClientSecurityDto(cryptoService.toBase64(projectId), "X-API-KEY", "*");
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public Project getProjectByKey(String projectKey) {
        String projectId;
        try {
            projectId = cryptoService.fromBase64(projectKey);
        } catch (Exception e) {
            throw new BadRequestException("Invalid project key");
        }

        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        return projectOp.get();
    }

    public ProjectDto getProjectById(String projectId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var projectOp = projectRepository.findById(projectId);
        if (projectOp.isEmpty()) {
            throw new ResourceNotFoundException("Project", "projectId", projectId);
        }

        var project = projectOp.get();
        if (project.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return projectMapper.toDto(project);
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }
}
