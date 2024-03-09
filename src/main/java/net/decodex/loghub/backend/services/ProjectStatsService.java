package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.GeneralProjectStatDto;
import net.decodex.loghub.backend.domain.mappers.PlatformMapper;
import net.decodex.loghub.backend.domain.mappers.ProjectReleaseMapper;
import net.decodex.loghub.backend.domain.models.ProjectRelease;
import net.decodex.loghub.backend.domain.models.elastic.ProjectStat;
import net.decodex.loghub.backend.exceptions.specifications.ForbiddenActionException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.repositories.ProjectRepository;
import net.decodex.loghub.backend.repositories.elastic.ProjectStatRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectStatsService {

    private final AuthenticationService authenticationService;
    private final ProjectStatRepository projectStatRepository;
    private final ProjectRepository projectRepository;

    private final PlatformMapper platformMapper;
    private final ProjectReleaseMapper projectReleaseMapper;
    private final LogSessionRepository logSessionRepository;

    public GeneralProjectStatDto getProjectStats(String projectId, Principal principal) {
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
            // Get the current time
            LocalDateTime currentTime = LocalDateTime.now();
            // Get the timestamp of the previous hour
            LocalDateTime lastDay = currentTime.minusHours(24);
            var dto = new GeneralProjectStatDto();
            dto.setName(project.getName());
            dto.setPlatform(platformMapper.toDto(project.getPlatform()));
            dto.setReleases(project.getReleases().stream().sorted(Comparator.comparing(ProjectRelease::getCreatedAt).reversed()).map(projectReleaseMapper::toDto).collect(Collectors.toList()));
            dto.setProjectId(projectId);
            var stats = projectStatRepository.findByProjectIdAndStartIntervalBetweenOrderByStartInterval(projectId, lastDay, currentTime);
            dto.setHourByHour(stats);
            var totalCrashFreeSessions = logSessionRepository.countByProjectAndCrashFreeTrue(project);
            var totalSessions = logSessionRepository.countByProject(project);
            var totalErrors = 0;
            var totalTransactions = 0;
            for(ProjectStat stat : stats) {
                totalErrors += stat.getErrors();
                totalTransactions += stat.getTransactions();
            }
            var lastSession = stats.getLast();
            double crashFreePercentage = lastSession != null ? lastSession.getCrashFreePercentage() : 0.0;
            double crashFreePercentageGain = lastSession != null ? lastSession.getCrashFreeGain() : 0.0;
            dto.setTotalCrashFreeSessions(totalCrashFreeSessions);
            dto.setTotalSessions(totalSessions);
            dto.setCrashFreePercentage(crashFreePercentage);
            dto.setCrashFreePercentageGain(crashFreePercentageGain);
            dto.setTotalErrors(totalErrors);
            dto.setTotalTransactions(totalTransactions);
            return dto;
        } else {
            throw new ForbiddenActionException("Not member of organization");
        }
    }

    private static double getCrashFreePercentageGain(int previousTotalSessions, int previousTotalCrashFreeSessions, int totalSessions) {
        double thresholdPercentage = 0.5; // Define your threshold percentage here
        double crashFreePercentageGain = 0.0;

        if (previousTotalSessions > 0) {
            double previousCrashFreePercentage = previousTotalCrashFreeSessions / (double) previousTotalSessions * 100;
            double percentageDiff = (totalSessions / (double) previousTotalSessions * 100) - previousCrashFreePercentage;

            // Adjust the percentage difference based on the threshold
            if (Math.abs(percentageDiff) <= thresholdPercentage) {
                crashFreePercentageGain = percentageDiff;
            } else {
                crashFreePercentageGain = (percentageDiff < 0) ? -thresholdPercentage : thresholdPercentage;
            }
        }
        return crashFreePercentageGain;
    }
}
