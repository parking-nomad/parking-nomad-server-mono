package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;

public interface SaveParkingUseCase {
    Long saveParking(final Long memberId, final SaveParkingRequest saveParkingRequest, final MultipartFile parkingImage);
}
