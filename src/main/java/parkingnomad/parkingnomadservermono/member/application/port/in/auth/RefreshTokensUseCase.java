package parkingnomad.parkingnomadservermono.member.application.port.in.auth;

import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;

public interface RefreshTokensUseCase {
    TokenResponse refreshTokens(final String refreshToken);
}
