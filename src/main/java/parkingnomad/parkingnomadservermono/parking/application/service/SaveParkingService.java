package parkingnomad.parkingnomadservermono.parking.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.parking.application.port.in.SaveParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;
import parkingnomad.parkingnomadservermono.parking.application.port.out.AddressLocator;
import parkingnomad.parkingnomadservermono.parking.application.port.out.ImageUploader;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEvent;
import parkingnomad.parkingnomadservermono.parking.application.port.out.event.ParkingCreateEventPublisher;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.exception.parking.LocationSearchException;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.CANT_FIND_ADDRESS;

@Service
@Transactional
public class SaveParkingService implements SaveParkingUseCase {

    private final ParkingRepository parkingRepository;
    private final AddressLocator addressLocator;
    private final ParkingCreateEventPublisher publisher;
    private final ImageUploader imageUploader;

    public SaveParkingService(
            final ParkingRepository parkingRepository,
            final AddressLocator addressLocator,
            final ParkingCreateEventPublisher publisher,
            final ImageUploader imageUploader
    ) {
        this.parkingRepository = parkingRepository;
        this.addressLocator = addressLocator;
        this.publisher = publisher;
        this.imageUploader = imageUploader;
    }

    @Override
    public Long saveParking(final Long memberId, final SaveParkingRequest saveParkingRequest, final MultipartFile parkingImage) {
        final double latitude = saveParkingRequest.latitude();
        final double longitude = saveParkingRequest.longitude();
        final String address = getAddressOrThrow(latitude, longitude);
        final Parking parking = Parking.createWithoutId(memberId, latitude, longitude, address);
        final String imageName = imageUploader.upload(parkingImage);
        parking.addImageName(imageName);
        final Long savedId = parkingRepository.save(parking).getId();
        publisher.publish(new ParkingCreateEvent(savedId, memberId));
        return savedId;
    }

    private String getAddressOrThrow(final double latitude, final double longitude) {
        return addressLocator.convertToAddress(latitude, longitude)
                .orElseThrow(() -> new LocationSearchException(CANT_FIND_ADDRESS, latitude, longitude));
    }
}
