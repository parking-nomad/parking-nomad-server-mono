package parkingnomad.parkingnomadservermono.common.exception.base;

public abstract class AuthException extends RuntimeException {
    protected final String code;

    protected AuthException(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
