package parkingnomad.parkingnomadservermono.parking.adaptor.in.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import parkingnomad.parkingnomadservermono.common.event.MemberResignEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteLatestParkingByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteParkingsByMemberIdUseCase;

@Component
public class MemberResignEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberResignEventListener.class);

    private final DeleteParkingsByMemberIdUseCase deleteParkingsByMemberIdUseCase;
    private final DeleteLatestParkingByMemberIdUseCase deleteLatestParkingByMemberIdUseCase;

    public MemberResignEventListener(
            final DeleteParkingsByMemberIdUseCase deleteParkingsByMemberIdUseCase,
            final DeleteLatestParkingByMemberIdUseCase deleteLatestParkingByMemberIdUseCase
    ) {
        this.deleteParkingsByMemberIdUseCase = deleteParkingsByMemberIdUseCase;
        this.deleteLatestParkingByMemberIdUseCase = deleteLatestParkingByMemberIdUseCase;
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMemberResignEvent(final MemberResignEvent event) {
        LOGGER.info("Member resign event handler just LISTENED!");
        deleteParkingsByMemberIdUseCase.deleteParkingsByMemberIdUseCase(event.memberId());
        deleteLatestParkingByMemberIdUseCase.deleteLatestParkingByMemberId(event.memberId());
    }
}
