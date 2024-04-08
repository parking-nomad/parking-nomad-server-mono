package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import parkingnomad.parkingnomadservermono.member.domain.UserInfo;
import parkingnomad.parkingnomadservermono.member.domain.oauth.provider.OAuthProvider;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;
import parkingnomad.parkingnomadservermono.support.BaseTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class KakaoOauthClientTest extends BaseTest {

    @Autowired
    KakaoOauthClient kakaoOauthClient;

    @Value("${spring.auth.kakao.test_id_token}")
    String testIdToken;

    @Value("${spring.auth.kakao.test_invalid_id_token}")
    String invalidIdToken;

    @Value("${spring.auth.kakao.test_expired_id_token}")
    String expiredToken;

    @Test
    @DisplayName("provider에 해당하는 client 객체인 경우 true를 반환한다")
    void isFitWithProvider() {
        //given
        final OAuthProvider kakao = OAuthProvider.kakao;

        //when
        final boolean result = kakaoOauthClient.isFitWithProvider(kakao.name());

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("provider에 해당하는 client 객체가 아닌 경우 false를 반환한다")
    void isFitWithInvalidProvider() {
        //given
        final String invalidProviderName = "google";

        //when
        final boolean result = kakaoOauthClient.isFitWithProvider(invalidProviderName);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("oauthInfo의 idToken을 파싱하여 유저정보를 가져온다.")
    void getValidatedUserInfo() {
        // given
        final String nickname = "이동훈";
        final OAuthInfo oAuthInfo = new OAuthInfo("kakao", testIdToken, "accessToken");

        // when
        final UserInfo validatedUserInfo = kakaoOauthClient.getValidatedUserInfo(oAuthInfo);

        // then
        assertThat(validatedUserInfo.getNickname()).isEqualTo(nickname);
    }

    @Test
    @DisplayName("oauthInfo의 idToken이 유효하지 않은 경우 예외가 발생한다.")
    void getValidatedUserInfoFromInvalidToken () {
        //given
        final OAuthInfo oAuthInfo = new OAuthInfo("kakao", invalidIdToken, "accessToken");

        //when & then
        assertThatThrownBy(() -> kakaoOauthClient.getValidatedUserInfo(oAuthInfo))
                .isInstanceOf(InvalidOpenIdException.class)
                .hasMessage("비정상적인 OIDC 입니다. {provider : kakao}");
    }

    @Test
    @DisplayName("oauthInfo의 idToken이 만료된 경우 예외가 발생한다.")
    void getValidatedUserInfoFromExpiredToken () {
        //given
        final OAuthInfo oAuthInfo = new OAuthInfo("kakao", expiredToken, "accessToken");

        //when & then
        assertThatThrownBy(() -> kakaoOauthClient.getValidatedUserInfo(oAuthInfo))
                .isInstanceOf(InvalidOpenIdException.class)
                .hasMessage("비정상적인 OIDC 입니다. {provider : kakao}");
    }
}
