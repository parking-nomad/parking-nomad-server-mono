package parkingnomad.parkingnomadservermono.member.domain.oauth.clients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OAuthInfoTest {
    @Test
    @DisplayName("시그니처가 없는 jwt를 반환한다.")
    void getOpenIdWithoutSignature() {
        //given
        final OAuthInfo oAuthInfo = new OAuthInfo("provider", "test1.test2.test3", "accessToken");

        //when
        final String openIdWithoutSignature = oAuthInfo.getOpenIdWithoutSignature();

        //then
        assertThat(openIdWithoutSignature).isEqualTo("test1\\.test2\\.");
    }

    @Test
    @DisplayName("openId 토큰의 형식이 일치 하지 않으면 예외가 발생한다.")
    void getOpenIdWithoutSignatureException() {
        //given
        final OAuthInfo oAuthInfo = new OAuthInfo("provider", "test1.test2", "accessToken");

        //when & then
        assertThatThrownBy(oAuthInfo::getOpenIdWithoutSignature)
                .isInstanceOf(InvalidOpenIdException.class)
                .hasMessage("비정상적인 OIDC 입니다. {provider : provider}");

    }
}
