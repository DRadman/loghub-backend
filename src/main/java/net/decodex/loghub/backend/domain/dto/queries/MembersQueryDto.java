package net.decodex.loghub.backend.domain.dto.queries;


import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MembersQueryDto {
    private String search = null;
    private InvitedSelector invited = InvitedSelector.ALL;
    private List<String> roleIds = null;
}
