package parkingnomad.parkingnomadservermono.parking.application.port.in;


import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;

public interface FindParkingByIdAndMemberIdUseCase {
    ParkingResponse findParkingByIdAndMemberId(final Long id, final Long memberId);
}
