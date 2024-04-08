package parkingnomad.parkingnomadservermono.parking.domain;

import org.apache.logging.log4j.util.Strings;
import parkingnomad.parkingnomadservermono.parking.exception.parking.InvalidFileNameException;
import parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingImageAlreadyExists;

import java.time.LocalDateTime;

import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.IMAGE_ALREADY_EXISTS;
import static parkingnomad.parkingnomadservermono.parking.exception.parking.ParkingErrorCode.INVALID_FILE_NAME;

public class Parking {
    private final Long id;
    private final Long memberId;
    private final Coordinates coordinates;
    private final String address;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private String image;

    public Parking(
            final Long id,
            final Long memberId,
            final Coordinates coordinates,
            final String address,
            final String image,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.coordinates = coordinates;
        this.address = address;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Parking createWithoutId(
            final Long memberId,
            final double latitude,
            final double longitude,
            final String address
    ) {
        return new Parking(null, memberId, Coordinates.from(latitude, longitude), address, null, null, null);
    }

    public static Parking createWithId(
            final Long id,
            final Long memberId,
            final double latitude,
            final double longitude,
            final String address,
            final String image,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        return new Parking(id, memberId, Coordinates.from(latitude, longitude), address, image, createdAt, updatedAt);
    }

    public void addImageName(final String imageName) {
        if (Strings.isBlank(imageName)) {
            throw new InvalidFileNameException(INVALID_FILE_NAME, imageName);
        }
        if (!Strings.isBlank(image)) {
            throw new ParkingImageAlreadyExists(IMAGE_ALREADY_EXISTS);
        }
        this.image = imageName;
    }

    public boolean isSameId(final Long id) {
        return this.id.equals(id);
    }

    public double getLatitude() {
        return coordinates.getLatitude();
    }

    public double getLongitude() {
        return coordinates.getLongitude();
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
