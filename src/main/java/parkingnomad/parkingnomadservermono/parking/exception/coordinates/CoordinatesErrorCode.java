package parkingnomad.parkingnomadservermono.parking.exception.coordinates;

public enum CoordinatesErrorCode {
    INVALID_LATITUDE("COORDINATE0001", "최소 위도는 -90, 최대 위도는 90입니다."),
    INVALID_LONGITUDE("COORDINATE0002", "최소 경도는 -180, 최대 경도는 180입니다.");

    private final String code;
    private final String message;

    CoordinatesErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
