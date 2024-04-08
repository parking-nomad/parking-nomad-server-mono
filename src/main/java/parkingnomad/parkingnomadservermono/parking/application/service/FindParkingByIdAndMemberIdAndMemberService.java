package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.FindParkingByIdAndMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentParkingException;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.NON_EXISTENT_PARKING;

@Service
@Transactional(readOnly = true)
public class FindParkingByIdAndMemberIdAndMemberService implements FindParkingByIdAndMemberIdUseCase {

    private final ParkingRepository parkingRepository;

    public FindParkingByIdAndMemberIdAndMemberService(final ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    public ParkingResponse findParkingByIdAndMemberId(final Long id, final Long memberId) {
        final Parking parking = parkingRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(() -> new NonExistentParkingException(NON_EXISTENT_PARKING, id));
        return ParkingResponse.from(parking);
    }
}
