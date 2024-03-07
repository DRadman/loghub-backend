package net.decodex.loghub.backend.domain.dto;

import lombok.*;

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
public class LogSourceDto implements Serializable {
    private String logSourceId;
    private String uniqueIdentifier;
    private String os = "unknown";
    private String osVersion = "unknown";
    private String environment = "unknown";
    private String ipAddress = "unknown";
    private String cpuInfo = "unknown";
    private String macAddress = "unknown";
    private ProjectReleaseDto release;
    private int maxRam = 0;
    private String architecture = "unknown";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}