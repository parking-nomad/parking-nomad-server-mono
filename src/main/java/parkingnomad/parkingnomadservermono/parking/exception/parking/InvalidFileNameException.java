package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class InvalidFileNameException extends BadRequestException {
    public InvalidFileNameException(final ParkingErrorCode errorCode, final String invalidFileName) {
        super(errorCode.getCode(), String.format(errorCode.getMessage(), invalidFileName));
    }
}
