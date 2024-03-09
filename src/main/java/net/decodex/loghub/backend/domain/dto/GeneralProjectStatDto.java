package net.decodex.loghub.backend.domain.dto;

import lombok.*;
import net.decodex.loghub.backend.domain.models.elastic.ProjectStat;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class GeneralProjectStatDto {
    private String projectId;
    private String name;
    private PlatformDto platform;
    private List<ProjectReleaseDto> releases;
    private List<ProjectStat> hourByHour;
    private long totalSessions;
    private long totalCrashFreeSessions;
    private double crashFreePercentage;
    private double crashFreePercentageGain;
    private int totalErrors;
    private int totalTransactions;
}
