package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Invitation}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvitationDto implements Serializable {
    private String invitationId;
    private String email;
    private RoleDto role;
    private LocalDateTime createdAt;
}