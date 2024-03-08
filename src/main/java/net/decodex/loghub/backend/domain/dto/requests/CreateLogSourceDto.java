package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.decodex.loghub.backend.domain.models.CpuInfo;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.LogSource}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CreateLogSourceDto implements Serializable {
    @NotNull
    @NotEmpty
    private String uniqueIdentifier;
    private String os = "unknown";
    private String osVersion = "unknown";
    private String environment = "unknown";
    private String ipAddress = "unknown";
    private CpuInfo cpuInfo;
    private String macAddress = "unknown";
    private long maxRam = 0;
    private String architecture = "unknown";
    private String releaseId = "unknown";
}