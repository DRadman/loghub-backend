package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.enums.PermissionValue;
import net.decodex.loghub.backend.enums.ResourceType;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Permission}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PermissionDto implements Serializable {
    private ResourceType type;
    private List<PermissionValue> values;
}