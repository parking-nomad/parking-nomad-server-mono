package parkingnomad.parkingnomadservermono.member.exception.oauth;

public abstract class OAuthException extends RuntimeException {
    protected final String code;
    protected final String provider;

    protected OAuthException(final String code, final String provider) {
        this.code = code;
        this.provider = provider;
    }

    public String getCode() {
        return code;
    }
}

