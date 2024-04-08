package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;

@Component
public class JpaParkingMapper {

    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    public Parking toDomainEntity(final JpaParkingEntity jpaParkingEntity) {
        return Parking.createWithId(
                jpaParkingEntity.getId(),
                jpaParkingEntity.getMemberId(),
                jpaParkingEntity.getCoordinate().getY(),
                jpaParkingEntity.getCoordinate().getX(),
                jpaParkingEntity.getAddress(),
                jpaParkingEntity.getImage(),
                jpaParkingEntity.getCreatedAt(),
                jpaParkingEntity.getUpdatedAt()
        );
    }

    public JpaParkingEntity toJpaEntity(final Parking parking) {
        final Coordinate coordinate = new Coordinate(parking.getLongitude(), parking.getLatitude());
        final Point point = GEOMETRY_FACTORY.createPoint(coordinate);
        point.setSRID(4326);
        return new JpaParkingEntity(parking.getMemberId(), point, parking.getAddress(), parking.getImage());
    }
}
