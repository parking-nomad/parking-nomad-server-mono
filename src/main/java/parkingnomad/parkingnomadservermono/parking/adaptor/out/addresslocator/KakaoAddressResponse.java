package parkingnomad.parkingnomadservermono.parking.adaptor.out.addresslocator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record KakaoAddressResponse(@JsonProperty("documents") List<Documents> documents) {

    public Optional<String> getAddressName() {
        if (documents.isEmpty()) {
            return Optional.empty();
        }
        final RoadAddress roadAddress = documents.get(0).roadAddress();
        if (Objects.isNull(roadAddress)) {
            return Optional.of(documents.get(0).address.addressName());
        }
        return Optional.of(roadAddress.addressName());
    }

    private record Documents(@JsonProperty("road_address") RoadAddress roadAddress,
                             @JsonProperty("address") Address address) {
    }

    private record RoadAddress(@JsonProperty("address_name") String addressName) {
    }

    private record Address(@JsonProperty("address_name") String addressName) {
    }
}
