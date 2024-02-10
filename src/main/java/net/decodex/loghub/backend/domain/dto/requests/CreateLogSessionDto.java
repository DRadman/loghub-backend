package net.decodex.loghub.backend.domain.dto.requests;

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
public class CreateLogSessionDto implements Serializable {
    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime endTime;
    private String networkType = "unknown";
    private String networkSpeed = "unknown";
    private boolean crashFree = true;
}