package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.DebugFile;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DebugFileRepository extends MongoRepository<DebugFile, String> {
    Optional<DebugFile> findByFile(@NonNull String file);
    List<DebugFile> deleteByProjectIn(@NonNull Collection<Project> projects);
}
