package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteLatestParkingByMemberIdAndParkingUseCaseTest extends BaseTestWithContainers {

    @Autowired
    LatestParkingRepository latestParkingRepository;

    @Autowired
    DeleteLatestParkingByMemberIdAndParkingUseCase useCase;

    @Test
    @DisplayName("memberId와 parkingId가 일치하는 LatestParking을 삭제한다.")
    void deleteLatestParkingByMemberIdAndParkingId() {
        //given
        final LocalDateTime now = LocalDateTime.now();
        latestParkingRepository.saveLatestParking(Parking.createWithId(1L, 2L, 20, 30, "address", "image", now, now));
        latestParkingRepository.saveLatestParking(Parking.createWithId(3L, 4L, 20, 30, "address", "image", now, now));
        latestParkingRepository.saveLatestParking(Parking.createWithId(5L, 6L, 20, 30, "address", "image", now, now));

        //when
        useCase.deleteLatestParkingByMemberIdAndParking(1L, 2L);

        //then
        final Optional<Parking> found = latestParkingRepository.findLatestParkingByMemberId(1L);
        assertThat(found).isEmpty();
    }
}
