package parkingnomad.parkingnomadservermono.common.exception.base;

public abstract class BadRequestException extends BaseException {
    public BadRequestException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
