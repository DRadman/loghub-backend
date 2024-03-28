package net.decodex.loghub.backend.services;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import net.decodex.loghub.backend.domain.dto.LogSessionWithSourceIdDto;
import net.decodex.loghub.backend.domain.dto.queries.LogSessionQueryDto;
import net.decodex.loghub.backend.domain.mappers.LogSessionMapper;
import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.domain.models.QLogSession;
import net.decodex.loghub.backend.exceptions.specifications.OrganizationNotPresentException;
import net.decodex.loghub.backend.repositories.LogSessionRepository;
import net.decodex.loghub.backend.utils.QueryEngine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@RequiredArgsConstructor
@Service
public class LogSessionService {

    private final LogSessionRepository logSessionRepository;
    private final LogSessionMapper logSessionMapper;
    private final AuthenticationService authenticationService;

    public Page<LogSessionWithSourceIdDto> findAll(Pageable pageable, LogSessionQueryDto logSessionQueryDto, Principal principal) {
        var user = authenticationService.getLoggedUser(principal.getName());
        if (user.getOrganization() == null) {
            throw new OrganizationNotPresentException();
        }

        var predicate = buildPredicate(user.getOrganization(), logSessionQueryDto);

        return logSessionRepository.findAll(predicate, pageable).map(logSessionMapper::toDtoWithProjectId);
    }

    private Predicate buildPredicate(Organization organization, LogSessionQueryDto queryDto) {
        var qEntity = QLogSession.logSession;
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

        // Add logSourceIds to predicate
        if (queryDto.getLogSourceIds() != null && !queryDto.getLogSourceIds().isEmpty()) {
            var list = queryDto.getLogSourceIds();
            if (list.size() == 1) {
                list.add("");
            }
            predicateBuilder.and(qEntity.source.logSourceId.in(list));
        }

        if (queryDto.getSearch() != null && !queryDto.getSearch().isEmpty()) {
            predicateBuilder.and(QueryEngine.parseQuery(queryDto.getSearch().strip()));
        }
        return predicateBuilder;
    }
}
