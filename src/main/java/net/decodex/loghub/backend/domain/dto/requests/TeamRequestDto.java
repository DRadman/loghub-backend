package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Team}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    private String slug;
}