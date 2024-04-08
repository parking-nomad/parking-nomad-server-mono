package parkingnomad.parkingnomadservermono.parking.application.port.in.dto;


import parkingnomad.parkingnomadservermono.parking.domain.Parking;

import java.time.LocalDateTime;

public record ParkingResponse(
        Long id,
        Long memberId,
        double latitude,
        double longitude,
        String address,
        String image,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ParkingResponse from(final Parking parking) {
        return new ParkingResponse(
                parking.getId(),
                parking.getMemberId(),
                parking.getLatitude(),
                parking.getLongitude(),
                parking.getAddress(),
                parking.getImage(),
                parking.getCreatedAt(),
                parking.getUpdatedAt()
        );
    }
}
