package net.decodex.loghub.backend.repositories.elastic;

import net.decodex.loghub.backend.domain.models.elastic.ProjectStat;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectStatRepository extends ElasticsearchRepository<ProjectStat, String> {
    @Nullable
    ProjectStat findFirstByProjectIdOrderByEndIntervalDesc(@NonNull String projectId);
    List<ProjectStat> findByProjectIdAndStartIntervalBetween(@NonNull String projectId, LocalDateTime startIntervalStart, LocalDateTime startIntervalEnd);

}
