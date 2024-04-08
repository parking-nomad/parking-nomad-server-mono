package parkingnomad.parkingnomadservermono.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;
import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;
import parkingnomad.parkingnomadservermono.common.exception.base.ForbiddenException;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException badRequestException) {
        ErrorResponse errorResponse = new ErrorResponse(badRequestException.getErrorCode(), badRequestException.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleForbiddenException(final ForbiddenException forbiddenException) {
        ErrorResponse errorResponse = new ErrorResponse(forbiddenException.getErrorCode(), forbiddenException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception exception) {
        final ErrorResponse errorResponse = ErrorResponse.internalServerError(exception.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
