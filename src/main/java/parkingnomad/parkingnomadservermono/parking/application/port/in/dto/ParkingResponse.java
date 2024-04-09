package parkingnomad.parkingnomadservermono.parking.application.port.in.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;

import java.time.LocalDateTime;

@Schema(description = "조회 된 주차 정보")
public record ParkingResponse(
        @Schema(description = "주차 정보 id")
        Long id,

        @Schema(description = "주차 정보를 등록한 member의 id")
        Long memberId,

        @Schema(description = "주차 정보의 위도")
        double latitude,

        @Schema(description = "주차 정보의 경도")
        double longitude,

        @Schema(description = "주차 정보의 주소")
        String address,

        @Schema(description = "주차 정보 사진 이미지 이름")
        String image,

        @Schema(description = "주차 정보 등록 된 시간")
        LocalDateTime createdAt,

        @Schema(description = "주차 정보 변경 된 시간")
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
