package net.decodex.loghub.backend.domain.models;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "projects")
@QueryEntity
public class Project {
    @MongoId
    private String projectId;

    @NotNull
    private String name;

    @DBRef(lazy = true)
    @NotNull
    private Organization organization;

    @DBRef(lazy = true)
    @NotNull
    private Platform platform;

    @DBRef(lazy = true)
    private List<Team> projectTeams;

    @NotNull
    private List<String> tags;

    @NotNull
    private List<String> environments;

    @DBRef(lazy = true)
    @NotNull
    private List<ProjectRelease> releases;

    @DBRef(lazy = true)
    @NotNull
    private List<DebugFile> debugFiles;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
