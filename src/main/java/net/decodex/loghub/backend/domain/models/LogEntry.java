package net.decodex.loghub.backend.domain.models;

import com.querydsl.core.annotations.QueryEntity;
import kotlin.Pair;
import lombok.*;
import net.decodex.loghub.backend.enums.LogEntryLevel;
import net.decodex.loghub.backend.enums.LogEntryStatus;
import net.decodex.loghub.backend.enums.LogEntryType;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "log_entries")
@QueryEntity
public class LogEntry {

    @MongoId
    private String entryId;

    @NotNull
    @Indexed(name = "type_index")
    private LogEntryType type = LogEntryType.BREADCRUMB;

    @NotNull
    @Indexed(name = "level_index")
    private LogEntryLevel level;

    @NotNull
    @Indexed(name = "status_index")
    private LogEntryStatus status = LogEntryStatus.NEW;

    @NotNull
    private String message = "";

    private String stackTrace;
    @Indexed(name = "tags_with_value_index")
    private List<Pair<String, String>> tagsWithValue = Collections.emptyList();

    private LocalDateTime timestamp;

    @DBRef(lazy = true)
    @NotNull
    @Indexed(name = "log_session_index")
    private LogSession logSession;

    @DBRef(lazy = true)
    @NotNull
    @Indexed(name = "log_source_index")
    private LogSource logSource;

    @NotNull
    private DeviceState deviceState;

    @NotNull
    private String environment = "unknown";

    @DBRef(lazy = true)
    @NotNull
    @Indexed(name = "project_index")
    private Project project;

    @DBRef(lazy = true)
    private User assignedTo;

    @NotNull
    private List<Comment> comments = new ArrayList<>();

    @DBRef(lazy = true)
    private ProjectRelease projectRelease;

    @Indexed
    private int hashValue = calculateHash();

    public int calculateHash() {
        // Example: Concatenate title, type, message, stackTrace and calculate hash
        return Objects.hash(level, message, stackTrace, environment, tagsWithValue);
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
