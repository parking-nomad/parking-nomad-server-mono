package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class NonExistentLatestParkingException extends BadRequestException {
    public NonExistentLatestParkingException(final ParkingErrorCode parkingErrorCode, final Long memberId) {
        super(parkingErrorCode.getCode(), String.format(parkingErrorCode.getMessage(), memberId));
    }
}
