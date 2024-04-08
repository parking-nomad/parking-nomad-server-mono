package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class LocationSearchException extends BadRequestException {
    public LocationSearchException(
            final ParkingErrorCode coordinatesErrorCode,
            final double latitude,
            final double longitude
    ) {
        super(coordinatesErrorCode.getCode(), String.format(coordinatesErrorCode.getMessage(), latitude, longitude));
    }
}
