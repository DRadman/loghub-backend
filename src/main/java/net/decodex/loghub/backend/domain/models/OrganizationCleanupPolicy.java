package net.decodex.loghub.backend.domain.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class OrganizationCleanupPolicy {
    private int archivePeriod = 30;
    private int cleanupPeriod = 90;
}
