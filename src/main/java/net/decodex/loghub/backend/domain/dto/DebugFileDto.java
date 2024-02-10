package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.enums.DebugFileType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.DebugFile}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DebugFileDto implements Serializable {
    private String debugFileId;
    private DebugFileType type;
    private String fileUrl;
    private String file;
    private LocalDateTime createdAt;
}