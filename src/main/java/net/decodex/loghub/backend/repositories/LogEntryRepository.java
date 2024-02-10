package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.LogEntry;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface LogEntryRepository extends MongoRepository<LogEntry, String> {
    List<LogEntry> deleteByProjectIn(@NonNull Collection<Project> projects);
}
