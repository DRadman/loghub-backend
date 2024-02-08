package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Role}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto implements Serializable {
    private String roleId;
    private boolean isInternal;
    private String name;
    private List<PermissionDto> permissions;
}