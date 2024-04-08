package parkingnomad.parkingnomadservermono.parking.adaptor.out.addresslocator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

public record KakaoAddressResponse(@JsonProperty("documents") List<Documents> documents) {

    public Optional<String> getAddressName() {
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(documents.get(0).roadAddress.addressName);
    }

    private record Documents(@JsonProperty("road_address") RoadAddress roadAddress) {

    }

    private record RoadAddress(@JsonProperty("address_name") String addressName) {
    }
}
