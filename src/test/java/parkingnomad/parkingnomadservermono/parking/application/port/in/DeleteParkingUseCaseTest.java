package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingDeleteEventPublisher;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteParkingUseCaseTest extends BaseTestWithContainers {

    @Autowired
    DeleteParkingUseCase useCase;

    @Autowired
    ParkingRepository repository;

    @MockBean
    ParkingDeleteEventPublisher eventPublisher;

    @Test
    @DisplayName("memberId와 parkingId를 통해 parking을 삭제한다.")
    void deleteParking() {
        //given
        doNothing().when(eventPublisher).publish(any(ParkingDeleteEvent.class));
        final Parking saved = repository.save(Parking.createWithoutId(1L, 20, 30, "address"));

        //when
        useCase.deleteParking(saved.getMemberId(), saved.getId());

        //then
        final Optional<Parking> found = repository.findById(saved.getId());
        assertThat(found).isEmpty();
        verify(eventPublisher, times(1)).publish(any(ParkingDeleteEvent.class));
    }

}
