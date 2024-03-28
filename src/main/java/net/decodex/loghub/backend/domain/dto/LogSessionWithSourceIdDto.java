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
public class LogSessionWithSourceIdDto implements Serializable {
    String sessionId;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String networkType;
    String networkSpeed;
    boolean crashFree;
    String projectId;
    String logSourceId;
}