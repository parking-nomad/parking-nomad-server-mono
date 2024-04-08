package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.FindLatestParkingByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentLatestParkingException;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.NON_EXISTENT_LATEST_PARKING;

@Service
@Transactional
public class FindLatestParkingByMemberIdService implements FindLatestParkingByMemberIdUseCase {

    private final LatestParkingRepository latestParkingRepository;
    private final ParkingRepository parkingRepository;

    public FindLatestParkingByMemberIdService(
            final LatestParkingRepository latestParkingRepository,
            final ParkingRepository parkingRepository
    ) {
        this.latestParkingRepository = latestParkingRepository;
        this.parkingRepository = parkingRepository;
    }

    @Override
    public ParkingResponse findLatestParkingByMemberId(final Long memberId) {
        final Parking parking = latestParkingRepository.findLatestParkingByMemberId(memberId)
                .orElseGet(() -> getParkingFromParkingRepository(memberId));
        return ParkingResponse.from(parking);
    }

    private Parking getParkingFromParkingRepository(final Long memberId) {
        final Parking parking = findLatestParkingByMemberIdOrThrow(memberId);
        latestParkingRepository.saveLatestParking(parking);
        return parking;
    }

    private Parking findLatestParkingByMemberIdOrThrow(final Long memberId) {
        return parkingRepository.findLatestParkingByMemberId(memberId)
                .orElseThrow(() -> new NonExistentLatestParkingException(NON_EXISTENT_LATEST_PARKING, memberId));
    }
}
