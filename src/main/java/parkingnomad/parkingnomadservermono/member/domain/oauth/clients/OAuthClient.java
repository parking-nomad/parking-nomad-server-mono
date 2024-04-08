package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;

import parkingnomad.parkingnomadservermono.member.domain.UserInfo;

public interface OAuthClient {
    boolean isFitWithProvider(String oAuthProviderName);

    OAuthInfo getOauthInfo(String code);

    UserInfo getValidatedUserInfo(OAuthInfo oauthInfo);
}

