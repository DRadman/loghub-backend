package net.decodex.loghub.backend.domain.dto;

import lombok.*;

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
public class ProjectDetailsDto implements Serializable {
    private String projectId;
    private String name;
    private PlatformDto platform;
    private List<String> tags;
    private List<String> environments;
    private List<ProjectReleaseDto> releases;
}