package net.decodex.loghub.backend.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class RegisterUserRequestDto implements Serializable {
    @NotNull
    @NotEmpty
    @Size(min = 3, message = "Username must be at least 3 characters long")
    private String username;

    @SecurePassword
    private String password;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;
}