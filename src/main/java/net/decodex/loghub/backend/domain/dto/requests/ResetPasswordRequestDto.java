package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import net.decodex.loghub.backend.annotations.SecurePassword;
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
public class ResetPasswordRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    private String hash;

    @NotNull
    @NotEmpty
    @SecurePassword
    private String newPassword;
}