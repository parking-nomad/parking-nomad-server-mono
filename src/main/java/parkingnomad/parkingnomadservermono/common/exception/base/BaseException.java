package parkingnomad.parkingnomadservermono.common.exception.base;

public abstract class BaseException extends RuntimeException {
    private final String errorCode;
    private final String message;

    public BaseException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
