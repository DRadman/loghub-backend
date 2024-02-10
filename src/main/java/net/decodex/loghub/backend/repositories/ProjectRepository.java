package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Organization;
import net.decodex.loghub.backend.domain.models.Platform;
import net.decodex.loghub.backend.domain.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> deleteByProjectId(@NonNull String projectId);
    boolean existsByNameAndOrganization(@NonNull String name, @NonNull Organization organization);
    List<Project> findByNameContainsIgnoreCaseAndOrganization(@NonNull String name, @NonNull Organization organization);
    List<Project> deleteByPlatform(@NonNull Platform platform);
}
