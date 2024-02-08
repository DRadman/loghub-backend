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

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "device_states")
@QueryEntity
public class DeviceState {
    @MongoId
    private String deviceStateId;

    private double cpuUtilization = 0;
    private double ramUsage = 0;
    private int freeRam = 0;

    @NotNull
    private String locale = "unknown";

    @NotNull
    private String timezone = "unknown";

    @DBRef(lazy = true)
    private LogEntry logEntry;

    @DBRef(lazy = true)
    @NotNull
    private LogSource device;
    // Other metrics

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
