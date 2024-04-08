package parkingnomad.parkingnomadservermono.member.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidRefreshTokenException;
import parkingnomad.parkingnomadservermono.common.jwt.TokenParser;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.RefreshTokenRepository;
import parkingnomad.parkingnomadservermono.member.application.service.RefreshTokenService;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.member.exception.member.NonExistentMemberException;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class RefreshTokensUseCaseTest extends BaseTestWithContainers {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RefreshTokenRepository refreshTokenAdaptor;

    @Autowired
    TokenParser tokenParser;

    @Test
    @DisplayName("리프레시토큰과 회원정보가 유효한 경우 사용된 리프레시토큰을 삭제하고 새로운 토큰들을 반환한다.")
    void refreshTokens() {
        //given
        final String sub = "sub";
        final String name = "name";
        final String refreshToken = "refreshToken";
        final Member member = memberRepository.saveMember(Member.createWithoutId(sub, name));
        refreshTokenAdaptor.save(refreshToken, member.getId());

        //when
        final TokenResponse tokenResponse = refreshTokenService.refreshTokens(refreshToken);

        //then
        final String newRefreshToken = tokenResponse.refreshToken();
        final String accessToken = tokenResponse.accessToken();
        final Long memberIdFromAccessToken = tokenParser.parseToMemberIdFromAccessToken(accessToken);
        final Long memberIdFromRefreshToken = refreshTokenAdaptor.findMemberIdByRefreshToken(newRefreshToken).get();
        final Member foundMember = memberRepository.findById(memberIdFromAccessToken).get();
        final Optional<Long> memberIdByDeletedRefreshToken = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken);

        assertSoftly(softAssertions -> {
            assertThat(accessToken).isNotBlank();
            assertThat(refreshToken).isNotBlank();
            assertThat(memberIdFromAccessToken).isEqualTo(memberIdFromRefreshToken);
            assertThat(foundMember.getName()).isEqualTo(name);
            assertThat(foundMember.getSub()).isEqualTo(sub);
            assertThat(memberIdByDeletedRefreshToken).isNotPresent();
        });
    }

    @ParameterizedTest
    @DisplayName("리프레시토큰이 blank인 경우 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"", "   "})
    void refreshTokensFailByBlankRefreshToken(final String refreshToken) {
        //when & then
        assertThatThrownBy(() -> refreshTokenService.refreshTokens(refreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("유효하지 않은 refreshToken 입니다.");
    }

    @Test
    @DisplayName("리프레시토큰을 통해 멤버정보를 찾지못한경우 예외가 발생한다.")
    void refreshTokensFailByUnSavedRefreshToken() {
        //given
        final String unSavedRefreshToken = "adfadfadsfadsfadsfad";

        //when & then
        assertThatThrownBy(() -> refreshTokenService.refreshTokens(unSavedRefreshToken))
                .isInstanceOf(InvalidRefreshTokenException.class)
                .hasMessage("유효하지 않은 refreshToken 입니다.");
    }

    @Test
    @DisplayName("리프레시토큰을 통해 얻은 멤버정보가 유효하지 않은 경우 예외가 발생한다.")
    void refreshTokensFailByNonExistentMember() {
        //given
        final String refreshToken = "refreshToken";
        refreshTokenAdaptor.save(refreshToken, 0L);

        //when & then
        assertThatThrownBy(() -> refreshTokenService.refreshTokens(refreshToken))
                .isInstanceOf(NonExistentMemberException.class)
                .hasMessageContaining("존재하지 않는 회원입니다. {input_member_id :");
    }
}
