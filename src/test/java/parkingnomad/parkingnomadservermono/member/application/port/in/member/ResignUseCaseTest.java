package parkingnomad.parkingnomadservermono.member.application.port.in.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import parkingnomad.parkingnomadservermono.common.event.MemberResignEvent;
import parkingnomad.parkingnomadservermono.member.application.port.out.event.MemberResignEventPublisher;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class ResignUseCaseTest extends BaseTestWithContainers {

    @Autowired
    MemberRepository repository;

    @MockBean
    MemberResignEventPublisher memberResignEventPublisher;

    @Autowired
    ResignUseCase useCase;

    @Test
    @DisplayName("회원을 삭제한다.")
    void resign() {
        //given
        doNothing().when(memberResignEventPublisher).publish(any(MemberResignEvent.class));
        final Member saved = repository.saveMember(Member.createWithoutId("sub", "dh"));

        //when
        useCase.resign(saved.getId());

        //then
        final boolean result = repository.isExistedMember(saved.getId());
        assertThat(result).isFalse();
    }
}
