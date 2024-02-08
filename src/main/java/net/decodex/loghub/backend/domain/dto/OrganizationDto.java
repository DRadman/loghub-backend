package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Organization}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrganizationDto implements Serializable {
    private String organizationId;
    private String picture;
    private String slug;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}