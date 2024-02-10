package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Platform;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> deleteByPlatform(@NonNull Platform platform);
}
