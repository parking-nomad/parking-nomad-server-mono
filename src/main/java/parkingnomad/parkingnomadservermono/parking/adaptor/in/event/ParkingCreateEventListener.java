package parkingnomad.parkingnomadservermono.parking.adaptor.in.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEvent;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
public class ParkingCreateEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingCreateEventListener.class);

    private final ParkingCreateEventBroker broker;

    public ParkingCreateEventListener(final ParkingCreateEventBroker broker) {
        this.broker = broker;
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleParkingCreateEvent(final ParkingCreateEvent parkingCreateEvent) {
        LOGGER.info("Parking create event handler just LISTENED!");
        broker.consume(parkingCreateEvent);
    }
}
