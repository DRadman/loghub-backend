package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Project}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    @MongoId
    private String platformId;
}