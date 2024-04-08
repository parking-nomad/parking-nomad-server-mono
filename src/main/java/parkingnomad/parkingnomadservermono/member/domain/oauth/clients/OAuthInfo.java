package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;

import com.fasterxml.jackson.annotation.JsonProperty;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;

public record OAuthInfo(
        String provider,
        @JsonProperty("id_token") String idToken,
        @JsonProperty("access_token") String accessToken
) {
    public static final String TOKEN_DELIMITER = "\\.";
    public static final int TOKEN_HEADER_INDEX = 0;
    public static final int TOKEN_PAYLOAD_INDEX = 1;
    public static final int SPLIT_TOKEN_LENGTH = 3;

    public static OAuthInfo of(final String provider, final OAuthInfo oAuthInfo) {
        return new OAuthInfo(provider, oAuthInfo.idToken, oAuthInfo.accessToken);
    }

    public String getOpenIdWithoutSignature() {
        final String[] splitToken = idToken.split(TOKEN_DELIMITER);
        if (splitToken.length != SPLIT_TOKEN_LENGTH) {
            throw new InvalidOpenIdException(provider);
        }
        return splitToken[TOKEN_HEADER_INDEX] + TOKEN_DELIMITER + splitToken[TOKEN_PAYLOAD_INDEX] + TOKEN_DELIMITER;
    }
}
