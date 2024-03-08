package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogSession;
import net.decodex.loghub.backend.domain.models.LogSource;
import net.decodex.loghub.backend.domain.models.Project;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LogSessionRepository extends MongoRepository<LogSession, String> {
    Optional<LogSession> findFirstBySourceOrderByStartTimeDesc(@NonNull LogSource source);
    void deleteByProjectIn(@NonNull Collection<Project> projects);

    List<LogSession> findByProjectAndStartTimeBeforeAndEndTimeBeforeAndEndTimeNotNullOrProjectAndEndTimeNull(@NonNull Project project, @NonNull LocalDateTime startTime, @NonNull LocalDateTime endTime, @NonNull Project project1);
}
