package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogSession;
import net.decodex.loghub.backend.domain.models.LogSource;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface LogSessionRepository extends MongoRepository<LogSession, String> {
    Optional<LogSession> findFirstBySourceOrderByStartTimeDesc(@NonNull LogSource source);
    void deleteByProjectIn(@NonNull Collection<Project> projects);
}
