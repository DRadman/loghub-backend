package net.decodex.loghub.backend.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(collection = "project_stats")
public class ProjectStat {
    @MongoId
    private String id;

    private String name;

    @Indexed
    private String projectId;

    @Indexed
    private LocalDateTime startInterval;

    @Indexed
    private LocalDateTime endInterval;

    private int numberOfSessions = 0;

    private int numberOfCrashFreeSessions = 0;

    private double crashFreePercentage = 0.0;

    private double crashFreeGain = 0.0;

    private int errors = 0;

    private int transactions = 0;
}
