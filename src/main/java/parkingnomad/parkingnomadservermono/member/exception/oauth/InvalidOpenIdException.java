package parkingnomad.parkingnomadservermono.member.exception.oauth;

import static parkingnomad.parkingnomadservermono.member.exception.oauth.OAuthExceptionCode.INVALID_OIDC;

public class InvalidOpenIdException extends OAuthException {

    private static final String FIXED_MESSAGE = "비정상적인 OIDC 입니다. {provider : %s}";

    public InvalidOpenIdException(final String provider) {
        super(INVALID_OIDC.getCode(), provider);
    }

    @Override
    public String getMessage() {
        return String.format(FIXED_MESSAGE, this.provider);
    }
}

