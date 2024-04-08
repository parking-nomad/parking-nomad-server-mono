package parkingnomad.parkingnomadservermono.common.jwt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import parkingnomad.parkingnomadservermono.common.exception.auth.ExpiredAccessTokenException;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidAccessTokenException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenParserTest {

    String key = "2d53d14bdd2a417d9afa466521109255";
    String invalidKey = "2d5asd4bdd2a417d9afa466521109255";

    @Test
    @DisplayName("멤버의 정보를 통해 토큰으로 파싱한다.")
    void parseToAccessTokenFrom() {
        //given
        final long memberId = 1L;
        final TokenParser tokenParser = new TokenParser(key, 900000);

        //when
        final String accessToken = tokenParser.parseToAccessTokenFrom(memberId);

        //then
        final long parsedMemberId = tokenParser.parseToMemberIdFromAccessToken(accessToken);
        assertThat(parsedMemberId).isEqualTo(memberId);
    }

    @Test
    @DisplayName("accessToken이 만료된 경우 예외가 발생한다.")
    void parseToMemberIdFromExpiredAccessToken() throws InterruptedException {
        //given
        final long memberId = 1L;
        final TokenParser tokenParser = new TokenParser(key, 1);
        final String expiredAccessToken = tokenParser.parseToAccessTokenFrom(memberId);
        Thread.sleep(1000);

        //when & then
        assertThatThrownBy(() -> tokenParser.parseToMemberIdFromAccessToken(expiredAccessToken))
                .isInstanceOf(ExpiredAccessTokenException.class)
                .hasMessage("만료된 access_token 입니다.");
    }

    @Test
    @DisplayName("accessToken이 만료된 경우 예외가 발생한다.")
    void parseToMemberIdFromInvalidAccessToken() throws InterruptedException {
        //given
        final long memberId = 1L;
        final TokenParser tokenParser = new TokenParser(key, 900);
        final String expiredAccessToken = tokenParser.parseToAccessTokenFrom(memberId);
        final TokenParser tokenParserWithInvalidKey = new TokenParser(invalidKey, 900);

        //when & then
        assertThatThrownBy(() -> tokenParserWithInvalidKey.parseToMemberIdFromAccessToken(expiredAccessToken))
                .isInstanceOf(InvalidAccessTokenException.class)
                .hasMessage("유효하지 않은 access_token 입니다.");
    }
}
