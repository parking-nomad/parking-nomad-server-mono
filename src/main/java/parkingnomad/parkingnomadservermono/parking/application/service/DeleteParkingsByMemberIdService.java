package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteParkingsByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;

@Service
@Transactional
public class DeleteParkingsByMemberIdService implements DeleteParkingsByMemberIdUseCase {

    private final ParkingRepository parkingRepository;

    public DeleteParkingsByMemberIdService(final ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    public void deleteParkingsByMemberIdUseCase(final Long memberId) {
        parkingRepository.deleteParkingsByMemberId(memberId);
    }
}
