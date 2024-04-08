package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking;

import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;

import java.util.Objects;
import java.util.Optional;

@Component
public class RedisLatestParkingRepository implements LatestParkingRepository {

    private final RedisLatestParkingRepositoryAdaptor latestParkings;
    private final RedisLatestParkingMapper mapper;

    public RedisLatestParkingRepository(
            final RedisLatestParkingRepositoryAdaptor latestParkings,
            final RedisLatestParkingMapper mapper
    ) {
        this.latestParkings = latestParkings;
        this.mapper = mapper;
    }

    @Override
    public void saveLatestParking(final Parking parking) {
        latestParkings.save(mapper.toRedisEntity(parking));
    }

    @Override
    public Optional<Parking> findLatestParkingByMemberId(final Long memberId) {
        final RedisLatestParkingEntity redisEntity = latestParkings.findById(memberId)
                .orElse(null);
        if (Objects.isNull(redisEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomainEntity(redisEntity));
    }

    @Override
    public void deleteLatestParkingByMemberId(final Long memberId) {
        latestParkings.deleteById(memberId);
    }
}
