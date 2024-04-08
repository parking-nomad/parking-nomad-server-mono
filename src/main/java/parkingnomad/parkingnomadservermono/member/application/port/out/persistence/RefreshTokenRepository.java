package parkingnomad.parkingnomadservermono.member.application.port.out.persistence;

import java.util.Optional;

public interface RefreshTokenRepository {
    void save(final String refreshToken, final Long memberId);

    Optional<Long> findMemberIdByRefreshToken(final String refreshToken);

    void deleteByRefreshToken(final String refreshToken);
}
