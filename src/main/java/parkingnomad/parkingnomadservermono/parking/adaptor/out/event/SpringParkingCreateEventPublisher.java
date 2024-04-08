package parkingnomad.parkingnomadservermono.parking.adaptor.out.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEventPublisher;

@Component
public class SpringParkingCreateEventPublisher implements ParkingCreateEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringParkingCreateEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ParkingCreateEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
