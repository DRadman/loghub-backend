package net.decodex.loghub.backend.domain.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.decodex.loghub.backend.annotations.IsEnum;
import net.decodex.loghub.backend.enums.PlatformType;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Platform}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlatformRequestDto implements Serializable {
    @NotNull
    @IsEnum(PlatformType.class)
    private PlatformType type;
    private String version;

    @NotNull
    private List<String> defaultTags;
}