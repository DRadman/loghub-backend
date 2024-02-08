package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Team}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TeamDto implements Serializable {
    private String teamId;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}