package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash(value = "latestParking", timeToLive = 60 * 60 * 24)
public record RedisLatestParkingEntity(
        Long id,
        double latitude,
        double longitude,
        String address,
        @Id
        Long memberId,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
