package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.domain.models.ProjectRelease;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;

public interface ProjectReleaseRepository extends MongoRepository<ProjectRelease, String> {
    boolean existsByVersionAndProject(@NonNull String version, @NonNull Project project);
    void deleteByProjectIn(@NonNull Collection<Project> projects);
}
