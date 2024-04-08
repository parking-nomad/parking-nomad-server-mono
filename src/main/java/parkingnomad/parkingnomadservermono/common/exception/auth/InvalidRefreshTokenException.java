package parkingnomad.parkingnomadservermono.common.exception.auth;


import parkingnomad.parkingnomadservermono.common.exception.base.AuthException;

public class InvalidRefreshTokenException extends AuthException {

    private static final String FIXED_MESSAGE = "유효하지 않은 refreshToken 입니다.";

    public InvalidRefreshTokenException(final String code) {
        super(code);
    }

    @Override
    public String getMessage() {
        return FIXED_MESSAGE;
    }
}
