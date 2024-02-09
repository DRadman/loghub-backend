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
@Document(collection = "users")
@QueryEntity
public class User {
    @MongoId
    private String userId;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
    private String email;

    @NotNull
    private String firstName = "";

    @NotNull
    private String lastName = "";

    private String profileIcon;
    private String profileIconUrl;

    private boolean isActivated = false;

    @DBRef
    @NotNull
    private Role role;

    @DBRef(lazy = true)
    @NotNull
    private List<Team> teams = new ArrayList<>();

    @DBRef(lazy = true)
    private Organization organization;

    private LocalDateTime lastLoginTime;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
