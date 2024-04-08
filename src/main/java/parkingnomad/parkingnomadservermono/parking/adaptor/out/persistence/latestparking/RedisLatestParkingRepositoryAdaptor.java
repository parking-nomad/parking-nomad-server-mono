package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.latestparking;

import org.springframework.data.repository.CrudRepository;

public interface RedisLatestParkingRepositoryAdaptor extends CrudRepository<RedisLatestParkingEntity, Long> {
}
