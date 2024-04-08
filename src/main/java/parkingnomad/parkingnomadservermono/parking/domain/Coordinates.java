package parkingnomad.parkingnomadservermono.parking.domain;

import parkingnomad.parkingnomadservermono.parking.exception.coordinates.InvalidCoordinatesException;

import static parkingnomad.parkingnomadservermono.parking.exception.coordinates.CoordinatesErrorCode.INVALID_LATITUDE;
import static parkingnomad.parkingnomadservermono.parking.exception.coordinates.CoordinatesErrorCode.INVALID_LONGITUDE;

public class Coordinates {
    private static final int MINIMUM_LATITUDE = -90;
    private static final int MAXIMUM_LATITUDE = 90;
    private static final int MINIMUM_LONGITUDE = -180;
    private static final int MAXIMUM_LONGITUDE = 180;

    private double latitude;
    private double longitude;

    private Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinates from(final double latitude, final double longitude) {
        if (latitude < MINIMUM_LATITUDE || latitude > MAXIMUM_LATITUDE) {
            throw new InvalidCoordinatesException(INVALID_LATITUDE, latitude);
        }

        if (longitude < MINIMUM_LONGITUDE || longitude > MAXIMUM_LONGITUDE) {
            throw new InvalidCoordinatesException(INVALID_LONGITUDE, longitude);
        }

        return new Coordinates(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
