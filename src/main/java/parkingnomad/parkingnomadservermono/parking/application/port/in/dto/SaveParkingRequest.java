package parkingnomad.parkingnomadservermono.parking.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "저장 할 주차 정보의 위도 경도")
public record SaveParkingRequest(
        @Schema(description = "위도", minimum = "-90", maximum = "90")
        double latitude,
        @Schema(description = "경도", minimum = "-180", maximum = "180")
        double longitude
) {
}
