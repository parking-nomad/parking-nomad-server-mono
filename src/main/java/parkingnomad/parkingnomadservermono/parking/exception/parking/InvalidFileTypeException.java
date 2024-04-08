package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class InvalidFileTypeException extends BadRequestException {
    public InvalidFileTypeException(final ParkingErrorCode parkingErrorCode, final String invalidFileType) {
        super(parkingErrorCode.getCode(), String.format(parkingErrorCode.getMessage(), invalidFileType));
    }
}
