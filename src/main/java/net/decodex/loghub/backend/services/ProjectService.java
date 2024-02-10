package net.decodex.loghub.backend.services;

import com.jlefebure.spring.boot.minio.MinioException;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.repositories.*;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final FileStorageService fileStorageService;
    private final LogSourceRepository logSourceRepository;
    private final LogEntryRepository logEntryRepository;
    private final LogSessionRepository logSessionRepository;
    private final ProjectReleaseRepository projectReleaseRepository;
    private final IssueRepository issueRepository;
    private final OrganizationRepository organizationRepository;
    private final TeamRepository teamRepository;
    private final DebugFileRepository debugFileRepository;


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
}
