package net.decodex.loghub.backend.domain.dto;

import lombok.*;

import java.io.Serializable;

/**
 * DTO for {@link net.decodex.loghub.backend.domain.models.Organization}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenResponseDto implements Serializable {
    String accessToken;
    String refreshToken;
    long expiresIn;
    long refreshExpiresIn;
}