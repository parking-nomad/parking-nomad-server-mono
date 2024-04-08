package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.springframework.data.domain.Pageable;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;

public interface FindParkingsByMemberIdUseCase {
    PageResponse<ParkingResponse> findParkingsByMemberId(final Pageable pageable, final Long memberId);
}
