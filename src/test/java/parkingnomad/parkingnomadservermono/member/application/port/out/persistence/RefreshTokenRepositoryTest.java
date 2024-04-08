package parkingnomad.parkingnomadservermono.member.application.port.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenRepositoryTest extends BaseTestWithContainers {

    @Autowired
    RefreshTokenRepository refreshTokenAdaptor;

    @Test
    @DisplayName("리프레시토큰과 멤버아이디를 저장한다.")
    void save() {
        //given
        final String refreshToken = "refreshToken";
        final long memberId = 1L;

        //when
        refreshTokenAdaptor.save(refreshToken, memberId);

        //then
        final Long foundMemberId = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken).get();
        assertThat(foundMemberId).isEqualTo(memberId);
    }

    @Test
    @DisplayName("저장된 리프레시토큰과 멤버아이디를 삭제한다.")
    void deleteByRefreshToken() {
        //given
        final String refreshToken = "refreshToken";
        final long memberId = 1L;
        refreshTokenAdaptor.save(refreshToken, memberId);

        //when
        refreshTokenAdaptor.deleteByRefreshToken(refreshToken);

        //then
        final Optional<Long> foundMemberId = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken);
        assertThat(foundMemberId).isNotPresent();
    }
}
