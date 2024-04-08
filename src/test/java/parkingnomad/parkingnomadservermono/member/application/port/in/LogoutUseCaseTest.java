package parkingnomad.parkingnomadservermono.member.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.RefreshTokenRepository;
import parkingnomad.parkingnomadservermono.member.application.service.LogoutService;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LogoutUseCaseTest extends BaseTestWithContainers {

    @Autowired
    LogoutService logoutService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("로그아웃을 하면 리프레시토큰을 삭제한다.")
    void logout() {
        //given
        final String refreshToken = "refreshToken";
        refreshTokenRepository.save(refreshToken, 20L);

        //when
        logoutService.logout(refreshToken);

        //then
        final Optional<Long> memberId = refreshTokenRepository.findMemberIdByRefreshToken(refreshToken);
        assertThat(memberId).isNotPresent();
    }
}
