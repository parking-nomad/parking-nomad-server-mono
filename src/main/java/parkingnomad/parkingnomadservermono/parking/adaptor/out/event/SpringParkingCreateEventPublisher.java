package parkingnomad.parkingnomadservermono.parking.adaptor.out.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEventPublisher;

@Component
public class SpringParkingCreateEventPublisher implements ParkingCreateEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringParkingCreateEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringParkingCreateEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ParkingCreateEvent event) {
        applicationEventPublisher.publishEvent(event);
        LOGGER.info("Parking create event is just PUBLISHED");
    }
}
