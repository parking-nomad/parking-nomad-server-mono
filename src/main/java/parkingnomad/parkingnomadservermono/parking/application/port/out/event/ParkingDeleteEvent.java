package parkingnomad.parkingnomadservermono.parking.application.port.out.event;

public record ParkingDeleteEvent(Long memberId, Long parkingId) {
}
