package parkingnomad.parkingnomadservermono.member.application.port.out.event;

import parkingnomad.parkingnomadservermono.common.event.MemberResignEvent;

public interface MemberResignEventPublisher {
    void publish(final MemberResignEvent memberResignEvent);
}
