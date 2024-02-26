package net.decodex.loghub.backend.services;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.GeneralProjectStatDto;
import net.decodex.loghub.backend.domain.mappers.PlatformMapper;
import net.decodex.loghub.backend.domain.mappers.ProjectReleaseMapper;
import net.decodex.loghub.backend.domain.models.elastic.ProjectStat;
import net.decodex.loghub.backend.exceptions.specifications.ForbiddenActionException;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.exceptions.specifications.ResourceNotFoundException;
import net.decodex.loghub.backend.repositories.ProjectRepository;
import net.decodex.loghub.backend.repositories.elastic.ProjectStatRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectStatsService {

    private final AuthenticationService authenticationService;
    private final ProjectStatRepository projectStatRepository;
    private final ProjectRepository projectRepository;

    private final PlatformMapper platformMapper;
    private final ProjectReleaseMapper projectReleaseMapper;

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
            dto.setReleases(project.getReleases().stream().map(projectReleaseMapper::toDto).collect(Collectors.toList()));
            dto.setProjectId(projectId);
            var stats = projectStatRepository.findByProjectIdAndStartIntervalBetweenOrderByStartInterval(projectId, lastDay, currentTime);
            var previousStats = projectStatRepository.findByProjectIdAndStartIntervalBetweenOrderByStartInterval(projectId, lastDay.minusHours(24), lastDay);
            dto.setHourByHour(stats);
            var totalCrashFreeSessions = 0;
            var totalSessions = 0;
            var previousTotalCrashFreeSessions = 0;
            var previousTotalSessions = 0;
            var totalErrors = 0;
            var totalTransactions = 0;
            for(ProjectStat stat : stats) {
                totalSessions += stat.getNumberOfSessions();
                totalCrashFreeSessions += stat.getNumberOfCrashFreeSessions();
                totalErrors += stat.getErrors();
                totalTransactions += stat.getTransactions();
            }
            for (ProjectStat stat: previousStats) {
                previousTotalSessions += stat.getNumberOfSessions();
                previousTotalCrashFreeSessions += stat.getNumberOfCrashFreeSessions();
            }
            double crashFreePercentage = totalSessions == 0 ? 0 : (double) totalCrashFreeSessions / totalSessions * 100;
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
}
