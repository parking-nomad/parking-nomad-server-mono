package parkingnomad.parkingnomadservermono.member.exception.oauth;

import static parkingnomad.parkingnomadservermono.member.exception.oauth.OAuthExceptionCode.INVALID_PROVIDER;

public class InvalidOAuthProviderException extends OAuthException {
    private static final String FIXED_MESSAGE = "provider와 일치하는 client가 존재하지 않습니다 {input_provider : %s}";

    public InvalidOAuthProviderException(final String inputProvider) {
        super(INVALID_PROVIDER.getCode(), inputProvider);
    }

    @Override
    public String getMessage() {
        return String.format(FIXED_MESSAGE, provider);
    }
}

