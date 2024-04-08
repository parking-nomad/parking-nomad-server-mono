package parkingnomad.parkingnomadservermono.common.exception.base;

public abstract class ForbiddenException extends BaseException {
    public ForbiddenException(final String errorCode, final String message) {
        super(errorCode, message);
    }
}
