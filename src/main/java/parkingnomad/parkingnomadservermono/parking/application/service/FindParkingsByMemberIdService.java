package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.application.port.in.FindParkingsByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FindParkingsByMemberIdService implements FindParkingsByMemberIdUseCase {

    private final ParkingRepository parkingRepository;

    public FindParkingsByMemberIdService(final ParkingRepository parkingRepository) {
        this.parkingRepository = parkingRepository;
    }

    @Override
    public PageResponse<ParkingResponse> findParkingsByMemberId(final Pageable pageable, final Long memberId) {
        final Slice<Parking> results = parkingRepository.findParkingsByMemberIdAndPage(pageable, memberId);
        final List<ParkingResponse> parkingResponses = results.stream()
                .map(ParkingResponse::from)
                .toList();
        return PageResponse.of(results, parkingResponses);
    }
}
