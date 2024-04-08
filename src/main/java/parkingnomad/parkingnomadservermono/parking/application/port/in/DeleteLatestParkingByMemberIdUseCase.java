package parkingnomad.parkingnomadservermono.parking.application.port.in;

public interface DeleteLatestParkingByMemberIdUseCase {
    void deleteLatestParkingByMemberId(final Long memberId);
}
