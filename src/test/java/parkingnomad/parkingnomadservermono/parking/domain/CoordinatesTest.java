package parkingnomad.parkingnomadservermono.parking.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import parkingnomad.parkingnomadservermono.parking.exception.coordinates.InvalidCoordinatesException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class CoordinatesTest {

    @Test
    @DisplayName("Coordinates를 생성한다.")
    void createCoordinates() {
        //given
        final double latitude = 20;
        final double longitude = 30;

        //when
        Coordinates coordinates = Coordinates.from(latitude, longitude);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(coordinates.getLatitude()).isEqualTo(latitude);
            softAssertions.assertThat(coordinates.getLongitude()).isEqualTo(longitude);
        });
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 위도를 통해 Coordinates를 생성하면 예외가 발생한다.")
    @ValueSource(doubles = {-90.1, 90.1})
    void createFailByInvalidLatitude(double invalidLatitude) {
        //given
        final double longitude = 30;

        //when & then
        assertThatThrownBy(() -> Coordinates.from(invalidLatitude, longitude))
                .isInstanceOf(InvalidCoordinatesException.class)
                .hasMessageContaining("최소 위도는 -90, 최대 위도는 90입니다.");
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 경도를 통해 Coordinates를 생성하면 예외가 발생한다.")
    @ValueSource(doubles = {-180.1, 180.1})
    void createCoordinatesWithInvalidLongitude(double invalidLongitude) {
        //given
        final double latitude = 20;

        //when & then
        assertThatThrownBy(() -> Coordinates.from(latitude, invalidLongitude))
                .isInstanceOf(InvalidCoordinatesException.class)
                .hasMessageContaining("최소 경도는 -180, 최대 경도는 180입니다.");
    }
}
