package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteLatestParkingByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;

@Service
@Transactional
public class DeleteLatestParkingByMemberIdService implements DeleteLatestParkingByMemberIdUseCase {

    private final LatestParkingRepository latestParkingRepository;

    public DeleteLatestParkingByMemberIdService(final LatestParkingRepository latestParkingRepository) {
        this.latestParkingRepository = latestParkingRepository;
    }

    @Override
    public void deleteLatestParkingByMemberId(final Long memberId) {
        latestParkingRepository.deleteLatestParkingByMemberId(memberId);
    }
}
