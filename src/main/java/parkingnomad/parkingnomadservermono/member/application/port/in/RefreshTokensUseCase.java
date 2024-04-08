package parkingnomad.parkingnomadservermono.member.application.port.in;

import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;

public interface RefreshTokensUseCase {
    TokenResponse refreshTokens(final String refreshToken);
}
