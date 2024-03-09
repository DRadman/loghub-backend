package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.domain.models.CpuInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.LogSource}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LogSourceWithOnlineStatusDto implements Serializable {
    private String logSourceId;
    private String uniqueIdentifier;
    private String os = "unknown";
    private String osVersion = "unknown";
    private String environment = "unknown";
    private String ipAddress = "unknown";
    private CpuInfo cpuInfo;
    private String macAddress = "unknown";
    private ProjectReleaseDto release;
    private long maxRam = 0;
    private String architecture = "unknown";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isOnline = false;
}