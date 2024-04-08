package parkingnomad.parkingnomadservermono.member.application.port.in;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import parkingnomad.parkingnomadservermono.common.jwt.TokenParser;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.RefreshTokenRepository;
import parkingnomad.parkingnomadservermono.member.application.service.SocialLoginService;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.member.domain.UserInfo;
import parkingnomad.parkingnomadservermono.member.domain.oauth.clients.OAuthClients;
import parkingnomad.parkingnomadservermono.member.domain.oauth.provider.OAuthProvider;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;

class SocialLoginUseCaseTest extends BaseTestWithContainers {

    SocialLoginService socialLoginService;
    @Autowired
    TokenParser tokenParser;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RefreshTokenRepository refreshTokenAdaptor;

    @MockBean
    OAuthClients oAuthClients;

    @BeforeEach
    void setUp() {
        socialLoginService = new SocialLoginService(oAuthClients, tokenParser, memberRepository, refreshTokenAdaptor);
    }

    @Test
    @DisplayName("새로운 회원이 소셜로그인을 하면 새로운 멤버의 정보가 저장되고, accessToken과 refreshToken을 반환한다.")
    void socialLoginByNewMember() {
        //given
        final UserInfo userInfo = UserInfo.of("sub", "kakao", "nickname");
        Mockito.when(oAuthClients.getUserInfo(anyString(), anyString())).thenReturn(userInfo);

        //when
        final TokenResponse response = socialLoginService.socialLogin("code", OAuthProvider.kakao);

        //then
        final String accessToken = response.accessToken();
        final String refreshToken = response.refreshToken();
        final Long memberIdFromAccessToken = tokenParser.parseToMemberIdFromAccessToken(accessToken);
        final Long memberIdFromRefreshToken = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken).get();
        final Member member = memberRepository.findById(memberIdFromAccessToken).get();
        assertSoftly(softAssertions -> {
            assertThat(accessToken).isNotBlank();
            assertThat(refreshToken).isNotBlank();
            assertThat(memberIdFromAccessToken).isEqualTo(memberIdFromRefreshToken);
            assertThat(member.getName()).isEqualTo("nickname");
            assertThat(member.getSub()).isEqualTo("kakaosub");
        });
    }


    @Test
    @DisplayName("기존 회원이 소셜로그인을 하면 멤버의 정보로 만들어진 accessToken과 refreshToken을 반환한다.")
    void socialLoginByExistedMember() {
        //given
        final Member savedMember = memberRepository.saveMember(Member.createWithoutId("providersub", "nickname"));
        final UserInfo userInfo = UserInfo.of("sub", "provider", "nickname");
        Mockito.when(oAuthClients.getUserInfo(anyString(), anyString())).thenReturn(userInfo);

        //when
        final TokenResponse response = socialLoginService.socialLogin("code", OAuthProvider.kakao);

        //then
        final String accessToken = response.accessToken();
        final String refreshToken = response.refreshToken();
        final Long memberIdFromAccessToken = tokenParser.parseToMemberIdFromAccessToken(accessToken);
        final Long memberIdFromRefreshToken = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken).get();
        final Member member = memberRepository.findById(memberIdFromAccessToken).get();
        assertSoftly(softAssertions -> {
            assertThat(memberIdFromAccessToken).isEqualTo(savedMember.getId());
            assertThat(memberIdFromRefreshToken).isEqualTo(savedMember.getId());
            assertThat(member.getName()).isEqualTo("nickname");
            assertThat(member.getSub()).isEqualTo("providersub");
        });
    }
}
