package net.decodex.loghub.backend.services;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Organization}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateOrganizationRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    String slug;
    @NotNull
    @NotEmpty
    private String name;
}