package parkingnomad.parkingnomadservermono.parking.adaptor.in.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteLatestParkingByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.SaveLatestParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEvent;

import java.util.Map;
import java.util.concurrent.*;

import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class ParkingCreateEventBroker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingCreateEventBroker.class);

    private final Map<Long, BlockingQueue<Long>> tasksByMemberId = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private final SaveLatestParkingUseCase saveLatestParkingUseCase;
    private final DeleteLatestParkingByMemberIdUseCase deleteLatestParkingByMemberIdUseCase;

    public ParkingCreateEventBroker(
            final SaveLatestParkingUseCase saveLatestParkingUseCase,
            final DeleteLatestParkingByMemberIdUseCase deleteLatestParkingByMemberIdUseCase
    ) {
        this.saveLatestParkingUseCase = saveLatestParkingUseCase;
        this.deleteLatestParkingByMemberIdUseCase = deleteLatestParkingByMemberIdUseCase;
    }

    public void consume(final ParkingCreateEvent parkingCreateEvent) {
        final Long memberId = parkingCreateEvent.memberId();
        final Long parkingId = parkingCreateEvent.parkingId();
        final BlockingQueue<Long> tasks = tasksByMemberId.get(memberId);
        if (isNull(tasks)) {
            final BlockingQueue<Long> newTasks = new LinkedBlockingQueue<>();
            tasksByMemberId.put(memberId, newTasks);
            newTasks.offer(parkingId);
            processBy(memberId);
            return;
        }
        tasks.offer(parkingId);
    }

    private void processBy(final Long memberId) {
        executorService.execute(() -> {
            final BlockingQueue<Long> tasks = tasksByMemberId.get(memberId);
            boolean isTasksEmpty = tasks.isEmpty();
            while (!isTasksEmpty) {
                try {
                    Long parkingId = tasks.poll(1, SECONDS);
                    if (isNull(parkingId)) {
                        isTasksEmpty = true;
                        removeTasksByMemberId(memberId);
                        deleteLatestParkingByMemberIdUseCase.deleteLatestParkingByMemberId(memberId);
                        LOGGER.info("Remove latest parking because of exception");
                    }
                    saveLatestParking(memberId, parkingId);
                    break;
                } catch (InterruptedException exception) {
                    LOGGER.error("PARKING CREATE EVENT BROKER ERROR OCCURRED | ERROR_MSG : {} | \n {} ", exception.getMessage(), exception.getStackTrace()[0]);
                }
            }
        });
    }

    private void saveLatestParking(final Long memberId, final Long parkingId) throws InterruptedException {
        final BlockingQueue<Long> tasks = tasksByMemberId.get(memberId);
        Long copiedParkingId = parkingId;
        while (!isNull(copiedParkingId)) {
            saveLatestParkingUseCase.saveLatestParking(copiedParkingId);
            copiedParkingId = tasks.poll(1, SECONDS);
        }
        removeTasksByMemberId(memberId);
    }

    private void removeTasksByMemberId(final Long memberId) {
        tasksByMemberId.remove(memberId);
    }
}
