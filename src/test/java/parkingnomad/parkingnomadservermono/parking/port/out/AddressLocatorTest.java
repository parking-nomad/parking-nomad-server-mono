package parkingnomad.parkingnomadservermono.parking.port.out;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.application.port.out.AddressLocator;
import parkingnomad.parkingnomadservermono.support.BaseTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AddressLocatorTest extends BaseTest {


    @Autowired
    AddressLocator addressLocator;

    @Test
    @DisplayName("좌표를 도로명 주소로 변환한다.")
    void convertToAddress() {
        //given
        String address = "경기도 안성시 죽산면 죽산초교길 69-4";
        double latitude = 37.0789561558879;
        double longitude = 127.423084873712;

        //when
        String result = addressLocator.convertToAddress(latitude, longitude).get();

        //then
        assertThat(result).isEqualTo(address);
    }

    @Test
    @DisplayName("좌표에 해당하는 주소를 찾지 못하는 경우 빈 Optional.empty를 반환한다.")
    void convertToAddressFail() {
        //given
        final double latitude = 40.748817;
        final double longitude = -73.985428;

        //when
        final Optional<String> result = addressLocator.convertToAddress(latitude, longitude);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("좌표에 해당하는 도로명 주소가 없는 경우 지번 주소를 반환한다.")
    void convertToOldAddress() {
        //given
        String address = "경기 성남시 중원구 상대원동 370-9";
        double latitude = 37.436179;
        double longitude = 127.180320;

        //when
        String result = addressLocator.convertToAddress(latitude, longitude).get();

        //then
        assertThat(result).isEqualTo(address);
    }
}
