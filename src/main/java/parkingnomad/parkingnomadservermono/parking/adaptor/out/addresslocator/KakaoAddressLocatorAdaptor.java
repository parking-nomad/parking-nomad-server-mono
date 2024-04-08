package parkingnomad.parkingnomadservermono.parking.adaptor.out.addresslocator;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "kakao-address-locator", url = "https://dapi.kakao.com/v2/local/geo/coord2address.json")
public interface KakaoAddressLocatorAdaptor {
    @GetMapping
    KakaoAddressResponse getAddress(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam("y") double latitude,
            @RequestParam("x") double longitude
    );
}
