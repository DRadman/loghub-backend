package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogSource;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface LogSourceRepository extends MongoRepository<LogSource, String>, QuerydslPredicateExecutor<LogSource> {
    Optional<LogSource> findByUniqueIdentifier(@NonNull String uniqueIdentifier);
    void deleteByProjectIn(@NonNull Collection<Project> projects);
}
