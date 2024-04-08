package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.member.application.port.out.oauth.KakaoOAuthInformationLoader;
import parkingnomad.parkingnomadservermono.member.domain.UserInfo;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;

@Component
public class KakaoOauthClient implements OAuthClient {

    private static final String NAME = "kakao";
    public static final String NICKNAME = "nickname";
    public static final String SUB = "sub";

    private final KakaoOAuthInformationLoader kakaoOAuthInformationLoader;

    @Value("${spring.auth.kakao.grant_type}")
    private String grandType;

    @Value("${spring.auth.kakao.client_id}")
    private String clientId;

    @Value("${spring.auth.kakao.client_secret}")
    private String clientSecret;

    @Value("${spring.auth.kakao.redirect_uri}")
    private String redirectUri;

    @Value("${spring.auth.kakao.token_request_uri}")
    private String tokenRequestUri;

    @Value("${spring.auth.kakao.token_issuer}")
    private String tokenIssuer;

    public KakaoOauthClient(final KakaoOAuthInformationLoader kakaoOAuthInformationLoader) {
        this.kakaoOAuthInformationLoader = kakaoOAuthInformationLoader;
    }

    @Override
    public boolean isFitWithProvider(final String oAuthProviderName) {
        return oAuthProviderName.equalsIgnoreCase(NAME);
    }

    @Override
    public OAuthInfo getOauthInfo(final String code) {
        return kakaoOAuthInformationLoader.getOAuthInfo(
                code,
                grandType,
                clientId,
                clientSecret,
                redirectUri,
                tokenRequestUri
        );
    }

    @Override
    public UserInfo getValidatedUserInfo(final OAuthInfo oauthInfo) {
        final String openIdWithoutSignature = oauthInfo.getOpenIdWithoutSignature();
        Jwt<Header, Claims> headerClaimsJwt = getParsedOpenIdToken(openIdWithoutSignature);

        final Claims body = headerClaimsJwt.getBody();
        final String nickname = body.get(NICKNAME, String.class);
        final String sub = body.get(SUB, String.class);

        return UserInfo.of(sub, NAME, nickname);
    }

    private Jwt<Header, Claims> getParsedOpenIdToken(final String openIdWithoutSignature) {
        try {
            return Jwts.parserBuilder()
                    .requireAudience(clientId)
                    .requireIssuer(tokenIssuer)
                    .build()
                    .parseClaimsJwt(openIdWithoutSignature);
        } catch (MissingClaimException | ExpiredJwtException exception) {
            throw new InvalidOpenIdException(NAME);
        }
    }
}
