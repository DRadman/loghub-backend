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
public class ChangePasswordRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    @SecurePassword
    private String oldPassword;

    @NotNull
    @NotEmpty
    @SecurePassword
    private String newPassword;
}