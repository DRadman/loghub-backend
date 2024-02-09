package net.decodex.loghub.backend.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.decodex.loghub.backend.annotations.IsMongoId;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Invitation}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateInvitationDto implements Serializable {
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @NotNull
    @IsMongoId
    private String roleId;
}