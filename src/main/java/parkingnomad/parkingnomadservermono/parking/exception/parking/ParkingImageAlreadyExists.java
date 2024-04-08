package parkingnomad.parkingnomadservermono.parking.exception.parking;

import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class ParkingImageAlreadyExists extends BadRequestException {
    public ParkingImageAlreadyExists(final ParkingErrorCode parkingErrorCode) {
        super(parkingErrorCode.getCode(), parkingErrorCode.getMessage());
    }
}
