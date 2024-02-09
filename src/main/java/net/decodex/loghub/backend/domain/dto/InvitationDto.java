package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.enums.InvitationStatus;

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
    private OrganizationDto organization;
    private InvitationStatus status;
    private LocalDateTime createdAt;
}