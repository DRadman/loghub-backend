package net.decodex.loghub.backend.domain.dto.queries;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LogSessionQueryDto {
    private String search = null;
    private List<String> logSourceIds = null;
    private List<String> projectIds = null;
}

