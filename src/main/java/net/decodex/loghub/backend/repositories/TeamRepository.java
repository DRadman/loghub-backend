package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.Project;
import net.decodex.loghub.backend.domain.models.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.List;

public interface TeamRepository extends MongoRepository<Team, String> {
    List<Team> findByProjectsIn(@NonNull Collection<Project> projects);
    List<Team> findByProjects(@NonNull Project projects);
}
