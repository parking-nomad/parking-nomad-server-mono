package parkingnomad.parkingnomadservermono.member.adaptor.out.refreshtoken;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)
public record RedisRefreshTokenEntity(@Id String refreshToken, Long memberId) {
}
