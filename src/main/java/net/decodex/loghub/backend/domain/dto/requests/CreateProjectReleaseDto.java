package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.ProjectRelease}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateProjectReleaseDto implements Serializable {
    @NotNull
    @NotEmpty
    private String version = "undefined";

    @Nullable
    private LocalDateTime releaseTimestamp = LocalDateTime.now();
}