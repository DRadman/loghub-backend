package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.LogSession}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LogSessionDto implements Serializable {
    private String sessionId;
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private String networkType = "unknown";
    private String networkSpeed = "unknown";
    private boolean crashFree = true;
}