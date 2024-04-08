package parkingnomad.parkingnomadservermono.parking.adaptor.out.addresslocator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.parking.application.port.out.AddressLocator;

import java.util.Optional;

@Component
public class KakaoAddressLocator implements AddressLocator {

    @Value("${spring.kakao.map.client_id}")
    private String clientId;

    @Value("${spring.kakao.map.authorization_prefix}")
    private String authorizationPrefix;

    private final KakaoAddressLocatorAdaptor kakaoAddressLocatorAdaptor;

    public KakaoAddressLocator(KakaoAddressLocatorAdaptor kakaoAddressLocatorAdaptor) {
        this.kakaoAddressLocatorAdaptor = kakaoAddressLocatorAdaptor;
    }

    @Override
    public Optional<String> convertToAddress(double latitude, double longitude) {
        String header = authorizationPrefix + " " + clientId;
        KakaoAddressResponse responses = kakaoAddressLocatorAdaptor.getAddress(header, latitude, longitude);
        return responses.getAddressName();
    }
}
