package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;

public class InvalidMemberIdException extends BadRequestException {

    public InvalidMemberIdException(final ParkingErrorCode parkingErrorCode, final Long invalidMemberId) {
        super(parkingErrorCode.getCode(), String.format(parkingErrorCode.getMessage(), invalidMemberId));
    }
}
