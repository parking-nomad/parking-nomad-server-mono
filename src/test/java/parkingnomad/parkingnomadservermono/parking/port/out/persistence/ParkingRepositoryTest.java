package parkingnomad.parkingnomadservermono.parking.port.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ParkingRepositoryTest extends BaseTestWithContainers {

    @Autowired
    ParkingRepository parkingRepository;

    private static Stream<Arguments> provideInvalidArgument() {
        return Stream.of(
                Arguments.of(0L, 0L),
                Arguments.of(0L, 1L),
                Arguments.of(1L, 0L)
        );
    }

    @Test
    @DisplayName("parking을 저장한다.")
    void save() {
        //given
        final long memberId = 1L;
        final int latitude = 20;
        final int longitude = 30;
        final String address = "address";
        final Parking parking = Parking.createWithoutId(memberId, latitude, longitude, address);

        //when
        final Parking savedParking = parkingRepository.save(parking);

        //then
        final Parking foundParking = parkingRepository.findById(savedParking.getId()).get();
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(foundParking.getId()).isEqualTo(savedParking.getId());
            softAssertions.assertThat(foundParking.getMemberId()).isEqualTo(memberId);
            softAssertions.assertThat(foundParking.getLatitude()).isEqualTo(latitude);
            softAssertions.assertThat(foundParking.getLongitude()).isEqualTo(longitude);
            softAssertions.assertThat(foundParking.getAddress()).isEqualTo(address);
        });
    }

    @Test
    @DisplayName("저장된 parking을 id와 memberId로 조회한다.")
    void findByIdAndMemberId() {
        //given
        final long memberId = 1L;
        final int latitude = 20;
        final int longitude = 30;
        final String address = "address";
        final Parking parking = Parking.createWithoutId(memberId, latitude, longitude, address);
        final Parking savedParking = parkingRepository.save(parking);

        //when
        final Parking found = parkingRepository.findByIdAndMemberId(savedParking.getId(), memberId).get();

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(found.getId()).isEqualTo(savedParking.getId());
            softAssertions.assertThat(found.getLatitude()).isEqualTo(latitude);
            softAssertions.assertThat(found.getLongitude()).isEqualTo(longitude);
            softAssertions.assertThat(found.getAddress()).isEqualTo(address);
            softAssertions.assertThat(found.getMemberId()).isEqualTo(memberId);
        });
    }

    @ParameterizedTest
    @DisplayName("id와 memberId가 일치하는 parking이 없는 경우 Optional.Empty를 반환한다.")
    @MethodSource("provideInvalidArgument")
    void findByIdAndMemberIdFail(final Long id, final Long memberId) {
        //given
        final Parking saved = parkingRepository.save(Parking.createWithoutId(1L, 20, 30, "address"));

        //when
        final Optional<Parking> found = parkingRepository.findByIdAndMemberId(id, memberId);

        //then
        final boolean equality = saved.getId().equals(id) && saved.getMemberId().equals(memberId);
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(equality).isFalse();
            softAssertions.assertThat(found).isEmpty();
        });
    }

    @Test
    @DisplayName("memberId가 일치하는 parking 중 가장 최근에 생성된 parking을 조회한다.")
    void findLatestParkingByMemberId() {
        //given
        parkingRepository.save(Parking.createWithoutId(1L, 20, 60, "address1"));
        parkingRepository.save(Parking.createWithoutId(1L, 30, 70, "address2"));
        parkingRepository.save(Parking.createWithoutId(2L, 50, 90, "address4"));
        final Parking target = parkingRepository.save(Parking.createWithoutId(1L, 40, 80, "address3"));

        //when
        final Parking parking = parkingRepository.findLatestParkingByMemberId(1L).get();

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(parking.getId()).isEqualTo(target.getId());
            softAssertions.assertThat(parking.getLatitude()).isEqualTo(target.getLatitude());
            softAssertions.assertThat(parking.getLongitude()).isEqualTo(target.getLongitude());
            softAssertions.assertThat(parking.getAddress()).isEqualTo(target.getAddress());
            softAssertions.assertThat(parking.getMemberId()).isEqualTo(target.getMemberId());
            softAssertions.assertThat(parking.getCreatedAt()).isEqualTo(target.getCreatedAt());
            softAssertions.assertThat(parking.getUpdatedAt()).isEqualTo(target.getUpdatedAt());
        });
    }

    @Test
    @DisplayName("id가 일치하는 Parking을 삭제한다.")
    void deleteByIdAndMemberId() {
        //given
        final Parking saved = parkingRepository.save(Parking.createWithoutId(1L, 20, 60, "address1"));

        //when
        parkingRepository.deleteById(saved.getId());

        //then
        final Optional<Parking> found = parkingRepository.findById(saved.getId());
        assertThat(found).isEmpty();
    }

}
