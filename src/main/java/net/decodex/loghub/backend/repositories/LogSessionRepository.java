package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogSession;
import net.decodex.loghub.backend.domain.models.LogSource;
import net.decodex.loghub.backend.domain.models.Project;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LogSessionRepository extends MongoRepository<LogSession, String>, QuerydslPredicateExecutor<LogSession> {
    Optional<LogSession> findFirstBySourceOrderByStartTimeDesc(@NonNull LogSource source);
    void deleteByProjectIn(@NonNull Collection<Project> projects);

    List<LogSession> findByProjectAndStartTimeBeforeAndEndTimeBetweenOrProjectAndEndTimeNull(@NonNull Project project, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTimeStart, @NonNull LocalDateTime endTimeEnd, @NonNull Project project1);

    boolean existsBySource_LogSourceIdAndEndTimeNull(@NonNull String logSourceId);

    long countByProject(@NonNull Project project);

    long countByProjectAndCrashFreeTrue(@NonNull Project project);
}
