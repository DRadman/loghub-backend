package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

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
}