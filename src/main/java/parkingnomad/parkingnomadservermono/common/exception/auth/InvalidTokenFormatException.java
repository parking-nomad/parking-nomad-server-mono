package parkingnomad.parkingnomadservermono.common.exception.auth;


import parkingnomad.parkingnomadservermono.common.exception.base.AuthException;

public class InvalidTokenFormatException extends AuthException {
    public InvalidTokenFormatException(final String code) {
        super(code);
    }

    @Override
    public String getMessage() {
        return "올바르지 않은 토큰 형식입니다.";
    }
}
