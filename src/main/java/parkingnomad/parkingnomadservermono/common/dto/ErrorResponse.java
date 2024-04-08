package parkingnomad.parkingnomadservermono.common.dto;

public record ErrorResponse(String code, String message) {

    public static ErrorResponse internalServerError(final String message) {
        return new ErrorResponse("INTERNAL_SERVER_ERROR", message);
    }

}
