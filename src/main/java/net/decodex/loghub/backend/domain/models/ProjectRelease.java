package net.decodex.loghub.backend.domain.models;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "project_releases")
@QueryEntity
public class ProjectRelease {
    @MongoId
    private String releaseId;

    @NotNull
    @Indexed
    private String version = "undefined";

    @CreatedDate
    private LocalDateTime releaseTimestamp;

    @DBRef(lazy = true)
    @Indexed(name = "project_index")
    private Project project;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
