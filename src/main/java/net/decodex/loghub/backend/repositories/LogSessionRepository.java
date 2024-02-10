package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogSession;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface LogSessionRepository extends MongoRepository<LogSession, String> {
    void deleteByProjectIn(@NonNull Collection<Project> projects);
}
