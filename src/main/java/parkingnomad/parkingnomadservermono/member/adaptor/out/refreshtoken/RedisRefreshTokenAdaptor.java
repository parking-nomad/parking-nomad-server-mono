package parkingnomad.parkingnomadservermono.member.adaptor.out.refreshtoken;

import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.RefreshTokenRepository;

import java.util.Optional;

import static java.util.Objects.isNull;

@Component
public class RedisRefreshTokenAdaptor implements RefreshTokenRepository {

    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    public RedisRefreshTokenAdaptor(final RedisRefreshTokenRepository redisRefreshTokenRepository) {
        this.redisRefreshTokenRepository = redisRefreshTokenRepository;
    }

    @Override
    public void save(final String refreshToken, final Long memberId) {
        final RedisRefreshTokenEntity redisRefreshTokenEntity = new RedisRefreshTokenEntity(refreshToken, memberId);
        redisRefreshTokenRepository.save(redisRefreshTokenEntity);
    }

    @Override
    public Optional<Long> findMemberIdByRefreshToken(final String refreshToken) {
        final RedisRefreshTokenEntity redisRefreshTokenEntity = redisRefreshTokenRepository.findById(refreshToken)
                .orElse(null);
        if (isNull(redisRefreshTokenEntity)) {
            return Optional.empty();
        }
        return Optional.of(redisRefreshTokenEntity.memberId());
    }

    @Override
    public void deleteByRefreshToken(final String refreshToken) {
        redisRefreshTokenRepository.deleteById(refreshToken);
    }
}
