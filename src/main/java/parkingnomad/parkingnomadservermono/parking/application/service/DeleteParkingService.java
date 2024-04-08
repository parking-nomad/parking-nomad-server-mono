package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEventPublisher;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentParkingException;
import parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode;

@Service
@Transactional
public class DeleteParkingService implements DeleteParkingUseCase {

    private final ParkingRepository parkingRepository;
    private final ParkingDeleteEventPublisher parkingDeleteEventPublisher;

    public DeleteParkingService(
            final ParkingRepository parkingRepository,
            final ParkingDeleteEventPublisher parkingDeleteEventPublisher
    ) {
        this.parkingRepository = parkingRepository;
        this.parkingDeleteEventPublisher = parkingDeleteEventPublisher;
    }

    @Override
    public void deleteParking(final Long memberId, final Long parkingId) {
        validate(memberId, parkingId);
        parkingRepository.deleteById(parkingId);
        parkingDeleteEventPublisher.publish(new ParkingDeleteEvent(memberId, parkingId));
    }

    private void validate(final Long memberId, final Long parkingId) {
        parkingRepository.findByIdAndMemberId(memberId, parkingId)
                .orElseThrow(() -> new NonExistentParkingException(ParkingErrorCode.NON_EXISTENT_PARKING, parkingId));
    }
}
