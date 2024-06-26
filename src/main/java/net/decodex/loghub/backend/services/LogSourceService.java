package net.decodex.loghub.backend.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSourceWithOnlineStatusDto;
import net.decodex.loghub.backend.domain.dto.queries.LogSourceQueryDto;
import net.decodex.loghub.backend.domain.mappers.LogSourceMapper;
import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.domain.models.QLogSource;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.repositories.LogSourceRepository;
import net.decodex.loghub.backend.utils.QueryEngine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class LogSourceService {

    private final LogSourceRepository logSourceRepository;
    private final LogSourceMapper logSourceMapper;
    private final LogSessionRepository logSessionRepository;
    private final AuthenticationService authenticationService;

    public Page<LogSourceWithOnlineStatusDto> findAll(Pageable pageable, LogSourceQueryDto logSourceQueryDto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        var predicate = buildPredicate(user.getOrganization(), logSourceQueryDto);

        var result = logSourceRepository.findAll(predicate, pageable).map(logSourceMapper::toDtoWithOnlineStatus);
        result.forEach(logSourceWithOnlineStatusDto -> logSourceWithOnlineStatusDto.setOnline(logSessionRepository.existsBySource_LogSourceIdAndEndTimeNull(logSourceWithOnlineStatusDto.getLogSourceId())));
        return result;
    }

    private Predicate buildPredicate(Organization organization, LogSourceQueryDto queryDto) {
        var qEntity = QLogSource.logSource;
        var projectIds = organization.getProjects().stream().map(Project::getProjectId).toList();
        if(projectIds.size() == 1) {
            projectIds.add("");
        }

        if (queryDto == null) {
            return qEntity.project.projectId.in(projectIds);
        }

        BooleanBuilder predicateBuilder = new BooleanBuilder();

        // Add projectIds predicate
        if (queryDto.getProjectIds() != null && !queryDto.getProjectIds().isEmpty()) {
            var list = queryDto.getProjectIds();
            if (list.size() == 1) {
                list.add("");
            }
            predicateBuilder.and(qEntity.project.projectId.in(list));
        } else {
            predicateBuilder.and(qEntity.project.projectId.in(projectIds));
        }

        // Add releaseIds predicate
        if (queryDto.getReleaseIds() != null && !queryDto.getReleaseIds().isEmpty()) {
            var list = queryDto.getReleaseIds();
            if (list.size() == 1) {
                list.add("");
            }
            predicateBuilder.and(qEntity.release.releaseId.in(list));
        }

        // Add environments predicate
        if (queryDto.getEnvironments() != null && !queryDto.getEnvironments().isEmpty()) {
            predicateBuilder.and(qEntity.environment.in(queryDto.getEnvironments()));
        }

        if (queryDto.getSearch() != null && !queryDto.getSearch().isEmpty()) {
            predicateBuilder.and(QueryEngine.parseQuery(queryDto.getSearch().strip()));
        }
        return predicateBuilder;
    }


}
