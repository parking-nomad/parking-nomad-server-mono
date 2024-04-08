package parkingnomad.parkingnomadservermono.member.exception.oauth;

public enum OAuthExceptionCode {
    INVALID_OIDC("OAUTH0001"),
    INVALID_PROVIDER("OAUTH0002");

    private final String code;

    OAuthExceptionCode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

