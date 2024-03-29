package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.ProjectDto;
import net.decodex.loghub.backend.domain.dto.requests.TeamRequestDto;
import net.decodex.loghub.backend.domain.dto.TeamDto;
import net.decodex.loghub.backend.domain.dto.UserDto;
import net.decodex.loghub.backend.domain.mappers.ProjectMapper;
import net.decodex.loghub.backend.domain.mappers.TeamMapper;
import net.decodex.loghub.backend.domain.mappers.UserMapper;
import net.decodex.loghub.backend.exceptions.specifications.ForbiddenActionException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceAlreadyExistsException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.OrganizationRepository;
import net.decodex.loghub.backend.repositories.ProjectRepository;
import net.decodex.loghub.backend.repositories.TeamRepository;
import net.decodex.loghub.backend.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final AuthenticationService authenticationService;
    private final TeamRepository teamRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;
    private final ProjectMapper projectMapper;
    private final OrganizationRepository organizationRepository;
    private final ProjectRepository projectRepository;

    public List<TeamDto> findAllTeams(String search, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        if (search == null || search.isEmpty()) {
            return user.getOrganization().getTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
        } else {
            var teams = teamRepository.findBySlugContainsIgnoreCaseAndOrganization(search, user.getOrganization());
            return teams.stream().map(teamMapper::toDto).collect(Collectors.toList());
        }
    }

    public List<TeamDto> findAllUserTeams(Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        return user.getTeams().stream().map(teamMapper::toDto).collect(Collectors.toList());
    }

    public boolean isSlugTaken(String slug, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        return teamRepository.existsBySlugAndOrganization(slug, user.getOrganization());
    }

    public List<UserDto> getTeamMembers(String teamId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return team.getMembers().stream().map(userMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<ProjectDto> getTeamProjects(String teamId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return team.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public TeamDto getTeamById(String teamId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            return teamMapper.toDto(team);
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<UserDto> addTeamMembers(String teamId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var usersToAdd = userRepository.findAllById(ids);
            team.getMembers().addAll(usersToAdd);
            usersToAdd.forEach(userToAdd -> userToAdd.getTeams().add(team));
            userRepository.saveAll(usersToAdd);
            teamRepository.save(team);

            return team.getMembers().stream().map(userMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<UserDto> removeTeamMembers(String teamId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var usersToRemove = userRepository.findAllById(ids);
            team.getMembers().removeAll(usersToRemove);
            usersToRemove.forEach(userToRemove -> userToRemove.getTeams().remove(team));
            userRepository.saveAll(usersToRemove);
            teamRepository.save(team);

            return team.getMembers().stream().map(userMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<ProjectDto> addTeamProjects(String teamId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var projectsToAdd = projectRepository.findAllById(ids);
            team.getProjects().addAll(projectsToAdd);
            projectsToAdd.forEach(project -> project.getProjectTeams().add(team));
            projectRepository.saveAll(projectsToAdd);
            teamRepository.save(team);

            return team.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public List<ProjectDto> removeTeamProjects(String teamId, List<String> ids, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            var projectsToRemove = projectRepository.findAllById(ids);
            team.getProjects().removeAll(projectsToRemove);
            projectsToRemove.forEach(project -> project.getProjectTeams().remove(team));
            projectRepository.saveAll(projectsToRemove);
            teamRepository.save(team);

            return team.getProjects().stream().map(projectMapper::toDto).collect(Collectors.toList());
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public TeamDto updateTeam(TeamRequestDto dto, String teamId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            team = teamMapper.partialUpdate(dto, team);
            teamRepository.save(team);

            return teamMapper.toDto(team);
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    public TeamDto createTeam(TeamRequestDto dto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        if (teamRepository.existsBySlugAndOrganization(dto.getSlug(), user.getOrganization())) {
            throw new ResourceAlreadyExistsException("Team", "slug", dto.getSlug());
        }
        var organizaiton = user.getOrganization();

        var team = teamMapper.toEntity(dto);
        team.setOrganization(organizaiton);
        team = teamRepository.save(team);

        organizaiton.getTeams().add(team);

        organizationRepository.save(organizaiton);
        return teamMapper.toDto(team);
    }

    public Object deleteTeam(String teamId, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }
        var teamOp = teamRepository.findById(teamId);
        if(teamOp.isEmpty()) {
            throw new ResourceNotFoundException("Team", "teamId", teamId);
        }

        var team = teamOp.get();
        var organization = user.getOrganization();
        if (team.getOrganization().getOrganizationId().equals(user.getOrganization().getOrganizationId())) {
            organization.getTeams().remove(team);
            organizationRepository.save(organization);
            var projects = team.getProjects();
            projects.forEach(project -> project.getProjectTeams().remove(team));
            var users = team.getMembers();
            users.forEach(userToRemoveTeamFrom -> userToRemoveTeamFrom.getTeams().remove(team));
            projectRepository.saveAll(projects);
            userRepository.saveAll(users);
            teamRepository.delete(team);

            return ResponseEntity.noContent().build();
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }
}
