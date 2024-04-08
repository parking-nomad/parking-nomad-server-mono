package parkingnomad.parkingnomadservermono.parking.exception.parking;

public enum ParkingErrorCode {
    INVALID_MEMBER_ID("PARKING0001", "member_id가 유효하지 않습니다. { invalid_member_id : %d }"),
    NON_EXISTENT_PARKING("PARKING0002", "존재하지 않는 parkingId 입니다. { invalid_parking_id : %d }"),
    MEMBER_LOADER("PARKING0003", "member loader와 정상적인 통신이 이뤄지지 않았습니다. { message_from_member_loader : %s }"),
    NON_EXISTENT_LATEST_PARKING("PARKING0004", "회원의 주차기록이 존재하지 않습니다. { member_id : %d }"),
    INVALID_FILE_TYPE("PARKING0005", "webp형식의 이미지만 업로드 가능합니다. { invalid_file_type: %s }"),
    INVALID_FILE_NAME("PARKING0006", "파일의 이름이 유효하지 않습니다. { invalid_file_name : %s }"),
    IMAGE_ALREADY_EXISTS("PARKING0007", "주차 이미지가 이미 존재합니다."),
    CANT_FIND_ADDRESS("COORDINATE0003", "좌표에 해당하는 주소를 찾을 수 없습니다. { lat : %.6f, lng : %.6f }");

    private final String code;
    private final String message;

    ParkingErrorCode(final String code, final String message) {
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
