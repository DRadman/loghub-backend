package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.enums.PermissionValue;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.OrganizationCleanupPolicy}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrganizationCleanupPolicyDto implements Serializable {
    private int archivePeriod;
    private int cleanupPeriod;
}