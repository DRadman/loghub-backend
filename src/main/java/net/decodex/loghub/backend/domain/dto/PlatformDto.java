package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.enums.PlatformType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Platform}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlatformDto implements Serializable {
    private String platformId;
    private PlatformType type;
    private String version = "undefined";
    private String iconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}