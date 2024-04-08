package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class NonExistentParkingException extends BadRequestException {
    public NonExistentParkingException(final ParkingErrorCode parkingErrorCode, final Long id) {
        super(parkingErrorCode.getCode(), String.format(parkingErrorCode.getMessage(), id));
    }
}
