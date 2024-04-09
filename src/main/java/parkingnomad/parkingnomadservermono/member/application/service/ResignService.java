package parkingnomad.parkingnomadservermono.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.common.event.MemberResignEvent;
import parkingnomad.parkingnomadservermono.member.application.port.in.member.ResignUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.out.event.MemberResignEventPublisher;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;

@Service
@Transactional
public class ResignService implements ResignUseCase {

    private final MemberRepository memberRepository;
    private final MemberResignEventPublisher publisher;

    public ResignService(
            final MemberRepository memberRepository,
            final MemberResignEventPublisher publisher
    ) {
        this.memberRepository = memberRepository;
        this.publisher = publisher;
    }

    @Override
    public void resign(final Long id) {
        memberRepository.deleteById(id);
        publisher.publish(new MemberResignEvent(id));
    }
}
