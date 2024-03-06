package net.decodex.loghub.backend.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProjectClientSecurityDto {
    private String securityToken;
    private String securityHeader;
    private String allowedDomains;
}
