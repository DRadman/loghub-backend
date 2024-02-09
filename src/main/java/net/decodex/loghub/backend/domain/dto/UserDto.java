package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.domain.dto.RoleDto;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.User}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto implements Serializable {
    private String userId;
    private String username;
    private LocalDateTime lastLoginTime;
    private String email;
    private String firstName;
    private String lastName;
    private String profileIconUrl;
    private boolean isActivated;
    private RoleDto role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}