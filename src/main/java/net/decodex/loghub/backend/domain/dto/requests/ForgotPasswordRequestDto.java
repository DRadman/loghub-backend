package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ForgotPasswordRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    private String username;
}