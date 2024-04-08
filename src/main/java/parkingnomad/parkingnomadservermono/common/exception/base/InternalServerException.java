package parkingnomad.parkingnomadservermono.common.exception.base;

public abstract class InternalServerException extends BaseException {
    public InternalServerException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
