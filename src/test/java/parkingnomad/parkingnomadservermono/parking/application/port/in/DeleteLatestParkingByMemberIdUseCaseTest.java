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

class DeleteLatestParkingByMemberIdUseCaseTest extends BaseTestWithContainers {

    @Autowired
    LatestParkingRepository latestParkingRepository;

    @Autowired
    DeleteLatestParkingByMemberIdUseCase deleteLatestParkingByMemberIdUseCase;

    @Test
    @DisplayName("memberId가 일치하는 latestParking정보를 삭제한다.")
    void deleteByMemberId() {
        //given
        final Long id = 1L;
        final Long memberId = 2L;
        final int latitude = 20;
        final int longitude = 30;
        final String address = "address";
        final String image = "image";
        final LocalDateTime now = LocalDateTime.now();
        final Parking parking = Parking.createWithId(id, memberId, latitude, longitude, address, image, now, now);
        latestParkingRepository.saveLatestParking(parking);

        //when
        deleteLatestParkingByMemberIdUseCase.deleteLatestParkingByMemberId(memberId);

        //then
        final Optional<Parking> found = latestParkingRepository.findLatestParkingByMemberId(memberId);
        assertThat(found).isEmpty();
    }
}
