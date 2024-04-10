package parkingnomad.parkingnomadservermono.parking.adaptor.in.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteLatestParkingByMemberIdAndParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEvent;

@Component
public class ParkingDeleteEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingDeleteEventListener.class);

    private final DeleteLatestParkingByMemberIdAndParkingUseCase deleteLatestParkingByMemberIdAndParkingUseCase;

    public ParkingDeleteEventListener(final DeleteLatestParkingByMemberIdAndParkingUseCase deleteLatestParkingByMemberIdAndParkingUseCase) {
        this.deleteLatestParkingByMemberIdAndParkingUseCase = deleteLatestParkingByMemberIdAndParkingUseCase;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleParkingDeleteEvent(final ParkingDeleteEvent parkingDeleteEvent) {
        LOGGER.info("Parking delete event handler just LISTENED!");
        final Long memberId = parkingDeleteEvent.memberId();
        final Long parkingId = parkingDeleteEvent.parkingId();
        deleteLatestParkingByMemberIdAndParkingUseCase.deleteLatestParkingByMemberIdAndParking(memberId, parkingId);
    }
}
