package parkingnomad.parkingnomadservermono.member.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.MemberResponse;
import parkingnomad.parkingnomadservermono.member.application.port.in.member.FindMemberUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.member.exception.member.InvalidMemberAccessException;
import parkingnomad.parkingnomadservermono.member.exception.member.NonExistentMemberException;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FindMemberUseCaseTest extends BaseTestWithContainers {

    @Autowired
    FindMemberUseCase findMemberUseCase;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("저장된 멤버를 조회한다.")
    void findMember() {
        //given
        Member savedMember = memberRepository.saveMember(Member.createWithoutId("sub", "member"));

        //when
        MemberResponse member = findMemberUseCase.findMember(savedMember.getId(), savedMember.getId());

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.id()).isEqualTo(savedMember.getId());
            softAssertions.assertThat(member.name()).isEqualTo(savedMember.getName());
        });
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 예외가 발생한다.")
    void findMemberFailNonExistentMember() {
        //given
        final long nonExistentMemberId = 0L;

        //when & then
        assertThatThrownBy(() -> findMemberUseCase.findMember(nonExistentMemberId, nonExistentMemberId))
                .isInstanceOf(NonExistentMemberException.class)
                .hasMessageContaining("존재하지 않는 회원입니다.");
    }


    @Test
    @DisplayName("로그인한 멤버와 조회하려는 멤버가 일치하지 않는 경우 예외가 발생한다.")
    void findMemberFailInvalidAccess() {
        //given
        final long loginMemberId = 1L;
        final long memberId = 0L;

        //when & then
        assertThatThrownBy(() -> findMemberUseCase.findMember(loginMemberId, memberId))
                .isInstanceOf(InvalidMemberAccessException.class)
                .hasMessageContaining("현재 로그인한 회원과 조회하려는 회원이 일치하지 않습니다.");
    }
}
