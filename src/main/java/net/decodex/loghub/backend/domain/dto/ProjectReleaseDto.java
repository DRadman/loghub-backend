package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.ProjectRelease}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectReleaseDto {
    private String releaseId;

    private String version;

    private LocalDateTime releaseTimestamp;
}
