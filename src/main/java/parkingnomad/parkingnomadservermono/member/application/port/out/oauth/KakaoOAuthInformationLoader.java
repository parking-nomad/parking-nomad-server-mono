package parkingnomad.parkingnomadservermono.member.application.port.out.oauth;

import parkingnomad.parkingnomadservermono.member.domain.oauth.clients.OAuthInfo;

public interface KakaoOAuthInformationLoader {
    OAuthInfo getOAuthInfo(
            final String code,
            final String grantType,
            final String clientId,
            final String clientSecret,
            final String redirectUri,
            final String tokenRequestUri
    );
}

