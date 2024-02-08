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
@Document(collection = "log_sources")
@QueryEntity
public class LogSource {

    @MongoId
    private String logSourceId;

    @NotNull
    @Indexed(unique = true)
    private String name;

    @NotNull
    private String os = "unknown";

    @NotNull
    private String osVersion = "unknown";

    @NotNull
    private String ipAddress = "unknown";

    @NotNull
    private String cpuInfo = "unknown";

    private int maxRam = 0;

    @NotNull
    private String architecture = "unknown";

    @DBRef(lazy = true)
    @NotNull
    private Project project;

    @DBRef(lazy = true)
    @NotNull
    private Platform platform;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
