package parkingnomad.parkingnomadservermono.member.adaptor.out.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.common.event.MemberResignEvent;
import parkingnomad.parkingnomadservermono.member.application.port.out.event.MemberResignEventPublisher;

@Component
public class SpringMemberResignEventPublisher implements MemberResignEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringMemberResignEventPublisher.class);

    private final ApplicationEventPublisher publisher;

    public SpringMemberResignEventPublisher(final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(final MemberResignEvent memberResignEvent) {
        publisher.publishEvent(memberResignEvent);
        LOGGER.info("Member resign event is just PUBLISHED");
    }
}
