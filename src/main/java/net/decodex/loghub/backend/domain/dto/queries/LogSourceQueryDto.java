package net.decodex.loghub.backend.domain.dto.queries;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LogSourceQueryDto {
    private String search = null;
    private List<String> releaseIds = null;
    private List<String> projectIds = null;
    private List<String> environments = null;
}
