package parkingnomad.parkingnomadservermono.member.application.port.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MemberRepositoryTest extends BaseTestWithContainers {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버를 저장한다.")
    void saveMember() {
        //given
        final String name = "name";
        final String sub = "sub";
        final Member member = Member.createWithoutId(sub, name);

        //when
        final Member savedMember = memberRepository.saveMember(member);

        //then
        final Member foundMember = memberRepository.findById(savedMember.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
            assertThat(foundMember.getName()).isEqualTo(name);
            assertThat(foundMember.getSub()).isEqualTo(sub);
        });
    }

    @Test
    @DisplayName("sub읉 통해 저장된 member를 조회한다.")
    void findBySub() {
        //given
        final String name = "name";
        final String sub = "sub";
        final Member member = Member.createWithoutId(sub, name);
        final Member savedMember = memberRepository.saveMember(member);

        //when
        final Member foundMember = memberRepository.findBySub(sub).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
            assertThat(foundMember.getName()).isEqualTo(name);
            assertThat(foundMember.getSub()).isEqualTo(sub);
        });
    }
}
