package net.decodex.loghub.backend.domain.models.elastic;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Document(indexName = "project_stat")
public class ProjectStat {
    @Id
    private String id;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "project_id", type = FieldType.Text)
    private String projectId;

    @Field(name = "start_interval", type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime startInterval;

    @Field(name = "end_interval", type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime endInterval;

    @Field(name = "number_of_sessions", type = FieldType.Integer)
    private int numberOfSessions = 0;

    @Field(name = "number_of_crash_free_sessions", type = FieldType.Integer)
    private int numberOfCrashFreeSessions = 0;

    @Field(name = "crash_free_percentage", type = FieldType.Double)
    private double crashFreePercentage = 0.0;

    @Field(name = "crash_free_gain", type = FieldType.Double)
    private double crashFreeGain = 0.0;

    @Field(name = "errors", type = FieldType.Integer)
    private int errors = 0;

    @Field(name = "transactions", type = FieldType.Integer)
    private int transactions = 0;
}
