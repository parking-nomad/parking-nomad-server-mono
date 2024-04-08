package parkingnomad.parkingnomadservermono.parking.application.port.out.persistence;


import parkingnomad.parkingnomadservermono.parking.domain.Parking;

import java.util.Optional;

public interface ParkingRepository {
    Parking save(final Parking parking);

    Optional<Parking> findById(final Long id);

    Optional<Parking> findByIdAndMemberId(final Long id, final Long memberId);

    Optional<Parking> findLatestParkingByMemberId(final Long memberId);

    void deleteById(final Long id);
}
