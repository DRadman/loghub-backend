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
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "log_sessions")
@QueryEntity
public class LogSession {
    @MongoId
    private String sessionId;

    @CreatedDate
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean crashFree = true;

    @DBRef(lazy = true)
    private Project project;

    @DBRef(lazy = true)
    @NotNull
    private LogSource source;

    @DBRef(lazy = true)
    @NotNull
    private List<LogEntry> logs = new ArrayList<>();

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
