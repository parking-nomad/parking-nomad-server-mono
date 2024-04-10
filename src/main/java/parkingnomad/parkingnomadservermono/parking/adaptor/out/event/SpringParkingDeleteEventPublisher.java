package parkingnomad.parkingnomadservermono.parking.adaptor.out.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEventPublisher;

@Component
public class SpringParkingDeleteEventPublisher implements ParkingDeleteEventPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringParkingDeleteEventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringParkingDeleteEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(final ParkingDeleteEvent event) {
        LOGGER.info("Parking delete event is just PUBLISHED");
        applicationEventPublisher.publishEvent(event);
    }
}
