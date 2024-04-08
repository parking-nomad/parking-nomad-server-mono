package parkingnomad.parkingnomadservermono.parking.exception.parking;


import parkingnomad.parkingnomadservermono.common.exception.base.InternalServerException;

public class ImageUploadFailException extends InternalServerException {
    public ImageUploadFailException(final String message) {
        super("FAIL_TO_UPLOAD", message);
    }
}
