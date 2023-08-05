package com.womentech.server.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
