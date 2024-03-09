package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.domain.dto.PlatformDto;
import net.decodex.loghub.backend.domain.models.ProjectRelease;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Project}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectDto implements Serializable {
    private String projectId;
    private String name;
    private PlatformDto platform;
    private List<String> environments;
    private List<ProjectReleaseDto> releases;
}