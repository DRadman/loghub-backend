package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MembersDto {
    private List<UserDto> users;
    private List<InvitationDto> invitations;
}
