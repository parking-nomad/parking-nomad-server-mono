package parkingnomad.parkingnomadservermono.parking.application.port.in;


import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;

public interface FindLatestParkingByMemberIdUseCase {
    ParkingResponse findLatestParkingByMemberId(final Long memberId);
}
