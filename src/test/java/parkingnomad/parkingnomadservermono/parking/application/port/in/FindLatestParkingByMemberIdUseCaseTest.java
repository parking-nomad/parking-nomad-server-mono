package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.LatestParkingRepository;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentLatestParkingException;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FindLatestParkingByMemberIdUseCaseTest extends BaseTestWithContainers {

    @Autowired
    FindLatestParkingByMemberIdUseCase useCase;

    @Autowired
    LatestParkingRepository latestParkingRepository;

    @Autowired
    ParkingRepository parkingRepository;

    @Test
    @DisplayName("latestParking에 최근주차정보가 저장되어 있는 경우 latestParkingRepository에서 parking을 조회한다.")
    void findLatestParkingFromLatestRepository() {
        //given
        final long id = 1L;
        final long memberId = 2L;
        final int latitude = 30;
        final int longitude = 40;
        final String address = "address";
        final String image = "image";
        final LocalDateTime now = LocalDateTime.now();
        final Parking parking = Parking.createWithId(id, memberId, latitude, longitude, address, image, now, now);
        latestParkingRepository.saveLatestParking(parking);

        //when
        final ParkingResponse found = useCase.findLatestParkingByMemberId(memberId);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(found.id()).isEqualTo(id);
            softAssertions.assertThat(found.memberId()).isEqualTo(memberId);
            softAssertions.assertThat(found.latitude()).isEqualTo(latitude);
            softAssertions.assertThat(found.longitude()).isEqualTo(longitude);
            softAssertions.assertThat(found.address()).isEqualTo(address);
            softAssertions.assertThat(found.image()).isEqualTo(image);
            softAssertions.assertThat(found.createdAt()).isEqualTo(now);
            softAssertions.assertThat(found.updatedAt()).isEqualTo(now);
        });
    }

    @Test
    @DisplayName("latestParking에 최근주차정보가 저장되어 있지 않은 경우 ParkingRepository에서 회원의 가장 최근 parking을 조회한다.")
    void findLatestParkingFromParkingRepository() {
        //given
        final long memberId = 2L;
        final int latitude = 30;
        final int longitude = 40;
        final String address = "address";
        final Parking parking = Parking.createWithoutId(memberId, latitude, longitude, address);
        final Parking saved = parkingRepository.save(parking);

        //when
        final ParkingResponse found = useCase.findLatestParkingByMemberId(memberId);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(found.id()).isEqualTo(saved.getId());
            softAssertions.assertThat(found.memberId()).isEqualTo(memberId);
            softAssertions.assertThat(found.latitude()).isEqualTo(latitude);
            softAssertions.assertThat(found.longitude()).isEqualTo(longitude);
            softAssertions.assertThat(found.address()).isEqualTo(address);
            softAssertions.assertThat(found.createdAt()).isEqualTo(saved.getCreatedAt());
            softAssertions.assertThat(found.updatedAt()).isEqualTo(saved.getUpdatedAt());
        });
    }

    @Test
    @DisplayName("latestParkingRepository와 ParkingRepository에 회원의 주차정보가 없는경우 에외가 발생한다.")
    void findLatestParkingFail() {
        //given
        final Long memberId = 0L;

        //when
        assertThatThrownBy(() -> useCase.findLatestParkingByMemberId(memberId))
                .isInstanceOf(NonExistentLatestParkingException.class)
                .hasMessageContaining("회원의 주차기록이 존재하지 않습니다");
    }
}
