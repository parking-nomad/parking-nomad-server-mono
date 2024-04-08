package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentParkingException;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SaveLatestParkingUseCaseTest extends BaseTestWithContainers {

    @Autowired
    ParkingRepository parkingRepository;

    @Autowired
    SaveLatestParkingUseCase saveLatestParkingUseCase;

    @Autowired
    LatestParkingRepository latestParkingRepository;

    @Test
    @DisplayName("id와 일치하는 주차정보를 최근 주차정보로 저장한다.")
    void saveLatestParking() {
        //given
        final Parking parking = parkingRepository.save(Parking.createWithoutId(1L, 30, 40, "address"));
        final Long latestParkingId = parking.getId();

        //when
        saveLatestParkingUseCase.saveLatestParking(latestParkingId);

        //then
        final Parking found = latestParkingRepository.findLatestParkingByMemberId(parking.getMemberId()).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(found.getId()).isEqualTo(parking.getId());
            softAssertions.assertThat(found.getMemberId()).isEqualTo(parking.getMemberId());
            softAssertions.assertThat(found.getLatitude()).isEqualTo(parking.getLatitude());
            softAssertions.assertThat(found.getLongitude()).isEqualTo(parking.getLongitude());
            softAssertions.assertThat(found.getAddress()).isEqualTo(parking.getAddress());
            softAssertions.assertThat(found.getCreatedAt()).isEqualTo(parking.getCreatedAt());
            softAssertions.assertThat(found.getUpdatedAt()).isEqualTo(parking.getUpdatedAt());
        });
    }

    @Test
    @DisplayName("id와 일치하는 주차정보가 없는 경우 예외가 발생한다.")
    void saveLatestParkingFail() {
        //given
        final Long nonexistentParkingId = 0L;

        //when
        assertThatThrownBy(() -> saveLatestParkingUseCase.saveLatestParking(nonexistentParkingId))
                .isInstanceOf(NonExistentParkingException.class)
                .hasMessageContaining("존재하지 않는 parkingId 입니다.");
    }
}
