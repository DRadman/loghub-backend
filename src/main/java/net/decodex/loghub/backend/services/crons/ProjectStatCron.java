package net.decodex.loghub.backend.services.crons;

import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.models.LogSession;
import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.domain.models.elastic.ProjectStat;
import net.decodex.loghub.backend.enums.LogEntryLevel;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.repositories.ProjectRepository;
import net.decodex.loghub.backend.repositories.elastic.ProjectStatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectStatCron {

    private final ProjectRepository projectRepository;
    private final ProjectStatRepository projectStatRepository;
    private final LogSessionRepository logSessionRepository;

    @Scheduled(cron = "@hourly")
    public void generateStatsForPastHour() {
        // Get the current time
        LocalDateTime currentTime = LocalDateTime.now();

        // Get the timestamp of the current hour
        LocalDateTime currentHourTimestamp = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), currentTime.getHour(), 0);

        // Get the timestamp of the previous hour
        LocalDateTime previousHourTimestamp = currentHourTimestamp.minusHours(1);

        // Find all projects
        List<Project> projects = projectRepository.findAll();

        // Process each project
        projects.forEach(project -> {
            // Find log sessions for the project within the previous hour
            List<LogSession> projectSessions = logSessionRepository.findByProjectAndStartTimeBeforeAndEndTimeBetweenOrProjectAndEndTimeNull(project, previousHourTimestamp, previousHourTimestamp, currentHourTimestamp, project);


            // Calculate statistics
            int numberOfActiveSessions = projectSessions.size();
            int numberOfCrashFreeSessions = (int) projectSessions.stream().filter(LogSession::isCrashFree).count();
            double crashFreePercentage = numberOfActiveSessions == 0 ? 0 : (double) numberOfCrashFreeSessions / numberOfActiveSessions * 100;

            int errors = projectSessions.stream().flatMapToInt(session ->
                    session.getLogs().stream().filter(logEntry ->
                            logEntry.getLevel() == LogEntryLevel.ERROR || logEntry.getLevel() == LogEntryLevel.FATAL
                    ).mapToInt(logEntry -> 1)).sum();

            int transactions = projectSessions.stream().flatMapToInt(session ->
                    session.getLogs().stream().filter(logEntry ->
                            logEntry.getLevel() != LogEntryLevel.ERROR && logEntry.getLevel() != LogEntryLevel.FATAL
                    ).mapToInt(logEntry -> 1)).sum();

            // Retrieve previous ProjectStat for this project
            var previousProjectStat = projectStatRepository.findFirstByProjectIdOrderByEndIntervalDesc(project.getProjectId());

            // Calculate crash-free session gain percentage with threshold
            double thresholdPercentage = 0.5; // Define your threshold percentage here
            double crashFreeGainPercentage = 0.0;
            if (previousProjectStat != null && previousProjectStat.getNumberOfSessions() > 0) {
                int previousCrashFreeSessions = previousProjectStat.getNumberOfCrashFreeSessions();
                double percentageDiff = ((double) numberOfCrashFreeSessions - previousCrashFreeSessions) / previousCrashFreeSessions * 100;

                // Adjust the percentage difference based on the threshold
                if (Math.abs(percentageDiff) <= thresholdPercentage) {
                    crashFreeGainPercentage = percentageDiff;
                } else {
                    crashFreeGainPercentage = (percentageDiff < 0) ? -thresholdPercentage : thresholdPercentage;
                }
            }

            // Create and save ProjectStat entity
            ProjectStat projectStat = new ProjectStat();
            projectStat.setProjectId(project.getProjectId());
            projectStat.setName(project.getName());
            projectStat.setStartInterval(previousHourTimestamp);
            projectStat.setEndInterval(currentHourTimestamp);
            projectStat.setNumberOfSessions(numberOfActiveSessions);
            projectStat.setNumberOfCrashFreeSessions(numberOfCrashFreeSessions);
            projectStat.setCrashFreePercentage(crashFreePercentage);
            projectStat.setCrashFreeGain(crashFreeGainPercentage);
            projectStat.setErrors(errors);
            projectStat.setTransactions(transactions);

            projectStatRepository.save(projectStat);
        });
    }
}
