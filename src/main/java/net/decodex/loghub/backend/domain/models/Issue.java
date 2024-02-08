package net.decodex.loghub.backend.domain.models;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import net.decodex.loghub.backend.enums.IssueStatus;
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
@Document(collection = "issues")
@QueryEntity
public class Issue {

    @MongoId
    private String issueId;

    @NotNull
    private String title = "undefined";

    private String description;

    @NotNull
    private IssueStatus status;

    private String externalUrl;

    private User assignedTo;

    @DBRef(lazy = true)
    private List<LogEntry> logEntries;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
