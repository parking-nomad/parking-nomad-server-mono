package parkingnomad.parkingnomadservermono.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class UserInfoTest {

    @Test
    @DisplayName("userInfo를 생성한다.")
    void createUserInfo() {
        //given
        final String sub = "sub";
        final String provider = "provider";
        final String nickname = "nickname";

        //when
        final UserInfo userInfo = UserInfo.of(sub, provider, nickname);

        //then
        assertSoftly(softAssertions -> {
            assertThat(userInfo.getNickname()).isEqualTo(nickname);
            assertThat(userInfo.getSub()).isEqualTo(sub);
            assertThat(userInfo.getProvider()).isEqualTo(provider);
        });
    }

    @ParameterizedTest
    @DisplayName("sub, provider, nickname 중 하나라도 null인 경우 예외가 발생한다.")
    @CsvSource(value = {"null, provider, nickname", "sub, null, nickname", "sub, provider, null"}, nullValues = {"null"})
    void ParameterizedTest(final String sub, final String provider, final String nickname) {
        // when & then
        assertThatThrownBy(() -> UserInfo.of(sub, provider, nickname))
                .isInstanceOf(InvalidOpenIdException.class)
                .hasMessageContaining("비정상적인 OIDC 입니다.");
    }
}
