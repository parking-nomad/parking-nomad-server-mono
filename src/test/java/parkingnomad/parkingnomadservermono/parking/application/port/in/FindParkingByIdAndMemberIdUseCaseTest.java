package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.NonExistentParkingException;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FindParkingByIdAndMemberIdUseCaseTest extends BaseTestWithContainers {

    @Autowired
    FindParkingByIdAndMemberIdUseCase useCase;

    @Autowired
    ParkingRepository parkingRepository;

    @Test
    @DisplayName("parkingId를 통해 저장된 parking을 조회한다.")
    void findParkingById() {
        //given
        final long memberId = 1L;
        final int latitude = 20;
        final int longitude = 30;
        final String address = "address";
        final Parking save = parkingRepository.save(Parking.createWithoutId(memberId, latitude, longitude, address));

        //when
        final ParkingResponse found = useCase.findParkingByIdAndMemberId(save.getId(), memberId);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(found.id()).isEqualTo(save.getId());
            softAssertions.assertThat(found.memberId()).isEqualTo(memberId);
            softAssertions.assertThat(found.latitude()).isEqualTo(latitude);
            softAssertions.assertThat(found.longitude()).isEqualTo(longitude);
            softAssertions.assertThat(found.address()).isEqualTo(address);
        });
    }

    @Test
    @DisplayName("parkingId에 해당하는 parking이 없는 경우 예외가 발생한다.")
    void findParkingFail() {
        //given
        final long invalidId = 0L;
        final long memberId = 1L;

        //when & then
        assertThatThrownBy(() -> useCase.findParkingByIdAndMemberId(invalidId, memberId))
                .isInstanceOf(NonExistentParkingException.class)
                .hasMessageContaining("존재하지 않는 parkingId 입니다.");
    }
}
