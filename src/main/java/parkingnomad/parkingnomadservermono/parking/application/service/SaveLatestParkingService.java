package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.SaveLatestParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentParkingException;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.NON_EXISTENT_PARKING;

@Service
@Transactional
public class SaveLatestParkingService implements SaveLatestParkingUseCase {

    private final ParkingRepository parkingRepository;
    private final LatestParkingRepository latestParkingRepository;

    public SaveLatestParkingService(
            final ParkingRepository parkingRepository,
            final LatestParkingRepository latestParkingRepository
    ) {
        this.parkingRepository = parkingRepository;
        this.latestParkingRepository = latestParkingRepository;
    }

    @Override
    public void saveLatestParking(final Long id) {
        final Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new NonExistentParkingException(NON_EXISTENT_PARKING, id));
        latestParkingRepository.saveLatestParking(parking);
    }
}
