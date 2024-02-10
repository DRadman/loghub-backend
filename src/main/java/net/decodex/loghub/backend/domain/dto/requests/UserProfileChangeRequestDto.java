package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileChangeRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    @Email
    private String email;
    @NotNull
    private String firstName = "";
    @NotNull
    private String lastName = "";
}