package net.decodex.loghub.backend.repositories;

import net.decodex.loghub.backend.domain.models.ProjectStat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectStatRepository extends MongoRepository<ProjectStat, String> {
    @Nullable
    ProjectStat findFirstByProjectIdOrderByEndIntervalDesc(@NonNull String projectId);
    List<ProjectStat> findByProjectIdAndStartIntervalBetweenOrderByStartInterval(@NonNull String projectId, LocalDateTime startIntervalStart, LocalDateTime startIntervalEnd);

}
