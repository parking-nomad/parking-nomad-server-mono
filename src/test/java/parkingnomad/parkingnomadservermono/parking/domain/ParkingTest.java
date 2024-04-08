package parkingnomad.parkingnomadservermono.parking.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class ParkingTest {

    @Test
    @DisplayName("id가 없는 Parkikng을 생성한다.")
    void createParkingWithoutId() {
        //given
        long memberId = 1L;
        int latitude = 20;
        int longitude = 30;
        String address = "address";

        //when
        Parking parking = Parking.createWithoutId(memberId, latitude, longitude, address);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(parking.getId()).isNull();
            softAssertions.assertThat(parking.getMemberId()).isEqualTo(memberId);
            softAssertions.assertThat(parking.getAddress()).isEqualTo(address);
            softAssertions.assertThat(parking.getLatitude()).isEqualTo(latitude);
            softAssertions.assertThat(parking.getLongitude()).isEqualTo(longitude);
        });
    }

    @Test
    @DisplayName("id를 포함한 Parking을 생성한다.")
    void createParkingWithId() {
        //given
        long id = 1L;
        long memberId = 2L;
        int latitude = 20;
        int longitude = 30;
        String address = "address";
        String image = "image";
        final LocalDateTime date = LocalDateTime.now();

        //when
        Parking parking = Parking.createWithId(id, memberId, latitude, longitude, address, image, date, date);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(parking.getId()).isEqualTo(id);
            softAssertions.assertThat(parking.getMemberId()).isEqualTo(memberId);
            softAssertions.assertThat(parking.getAddress()).isEqualTo(address);
            softAssertions.assertThat(parking.getLatitude()).isEqualTo(latitude);
            softAssertions.assertThat(parking.getLongitude()).isEqualTo(longitude);
            softAssertions.assertThat(parking.getCreatedAt()).isEqualTo(date);
            softAssertions.assertThat(parking.getUpdatedAt()).isEqualTo(date);
        });
    }

    @Test
    @DisplayName("parking에 이미지를 추가한다")
    void addImage() {
        //given
        final Parking parking = Parking.createWithoutId(1L, 20, 30, "address");
        final String imageName = "imageName";

        //when
        parking.addImageName(imageName);

        //then
        assertThat(parking.getImage()).isEqualTo(imageName);
    }

    private static Stream<Arguments> provideParkingIdAndExpect() {
        return Stream.of(
                Arguments.of(1L, true),
                Arguments.of(2L, false)
        );
    }

    @ParameterizedTest
    @DisplayName("parking의 id를 비교한다.")
    @MethodSource("provideParkingIdAndExpect")
    void isSameId(final Long compareId, final boolean expected) {
        //given
        final LocalDateTime now = LocalDateTime.now();
        final Parking parking = Parking.createWithId(1L, 2L, 20, 30, "address", "image", now, now);

        //when
        final boolean result = parking.isSameId(compareId);

        //then
        assertThat(result).isEqualTo(expected);
    }
}
