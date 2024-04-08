package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;

import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.member.domain.UserInfo;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOAuthProviderException;

import java.util.List;

@Component
public class OAuthClients {
    private final List<OAuthClient> oAuthClients;

    public OAuthClients(final List<OAuthClient> oAuthClients) {
        this.oAuthClients = oAuthClients;
    }

    public UserInfo getUserInfo(final String code, final String oAuthProviderName) {
        final OAuthClient oAuthClient = findClient(oAuthProviderName);
        final OAuthInfo oauthInfo = oAuthClient.getOauthInfo(code);
        return oAuthClient.getValidatedUserInfo(oauthInfo);
    }

    private OAuthClient findClient(final String oAuthProviderName) {
        return oAuthClients.stream()
                .filter(oAuthClient -> oAuthClient.isFitWithProvider(oAuthProviderName))
                .findAny()
                .orElseThrow(() -> new InvalidOAuthProviderException(oAuthProviderName));
    }
}
