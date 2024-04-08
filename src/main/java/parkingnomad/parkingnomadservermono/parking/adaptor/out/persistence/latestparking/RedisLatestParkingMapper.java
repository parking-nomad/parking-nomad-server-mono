package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking;

import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;

@Component
public class RedisLatestParkingMapper {
    public RedisLatestParkingEntity toRedisEntity(final Parking parking) {
        return new RedisLatestParkingEntity(
                parking.getId(),
                parking.getLatitude(),
                parking.getLongitude(),
                parking.getAddress(),
                parking.getMemberId(),
                parking.getImage(),
                parking.getCreatedAt(),
                parking.getUpdatedAt()
        );
    }

    public Parking toDomainEntity(final RedisLatestParkingEntity redisEntity) {
        return Parking.createWithId(
                redisEntity.id(),
                redisEntity.memberId(),
                redisEntity.latitude(),
                redisEntity.longitude(),
                redisEntity.address(),
                redisEntity.image(),
                redisEntity.createdAt(),
                redisEntity.updatedAt()
        );
    }
}
