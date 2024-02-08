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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "organizations")
@QueryEntity
public class Organization {
    @MongoId
    private String organizationId;

    @Indexed(unique = true)
    @NotNull
    private String slug;

    @NotNull
    private String name;

    @NotNull
    private String picture;

    @DBRef(lazy = true)
    @NotNull
    private User owner;

    @DBRef(lazy = true)
    private List<User> members = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Team> teams = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Project> projects = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Invitation> invitations = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
