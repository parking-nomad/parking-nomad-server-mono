package parkingnomad.parkingnomadservermono.member.adaptor.out.oauth;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import parkingnomad.parkingnomadservermono.member.application.port.out.oauth.KakaoOAuthInformationLoader;
import parkingnomad.parkingnomadservermono.member.domain.oauth.clients.OAuthInfo;

import java.net.URI;

@Component
public class RestTemplateKakaoOauthInformationLoader implements KakaoOAuthInformationLoader {

    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String PROVIDER = "kakao";
    private static final String CODE = "code";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String REDIRECT_URI = "redirect_uri";

    @Override
    public OAuthInfo getOAuthInfo(
            final String code,
            final String grantType,
            final String clientId,
            final String clientSecret,
            final String redirectUri,
            final String tokenRequestUri
    ) {
        final MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add(CODE, code);
        parameter.add(GRANT_TYPE, grantType);
        parameter.add(CLIENT_ID, clientId);
        parameter.add(CLIENT_SECRET, clientSecret);
        parameter.add(REDIRECT_URI, redirectUri);

        final OAuthInfo oAuthInfo = restTemplate.postForObject(URI.create(tokenRequestUri), parameter, OAuthInfo.class);
        return OAuthInfo.of(PROVIDER, oAuthInfo);
    }
}
