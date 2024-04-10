package parkingnomad.parkingnomadservermono.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;
import parkingnomad.parkingnomadservermono.common.exception.base.AuthException;
import parkingnomad.parkingnomadservermono.common.exception.base.BadRequestException;
import parkingnomad.parkingnomadservermono.common.exception.base.ForbiddenException;

import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    private final ObjectMapper objectMapper;

    public GlobalControllerAdvice(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleBadRequestException(final BadRequestException badRequestException) {
        ErrorResponse errorResponse = new ErrorResponse(badRequestException.getErrorCode(), badRequestException.getMessage());
        LOGGER.info("BAD REQUEST | CODE : {} | ERROR_MSG : {}", badRequestException.getErrorCode(), badRequestException.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException authException) {
        final ErrorResponse errorResponse = new ErrorResponse(authException.getCode(), authException.getMessage());
        LOGGER.info("UNAUTHORIZATION | CODE : {} | ERROR_MSG : {}", authException.getCode(), authException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleForbiddenException(final ForbiddenException forbiddenException) {
        ErrorResponse errorResponse = new ErrorResponse(forbiddenException.getErrorCode(), forbiddenException.getMessage());
        LOGGER.info("FORBIDDEN | CODE : {} | ERROR_MSG : {}", forbiddenException.getErrorCode(), forbiddenException.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleFeignException(final FeignException exception) throws JsonProcessingException {
        final String innerMessage = parseFeignException(exception);
        final ErrorResponse errorResponse = new ErrorResponse("FEIGN_EXCEPTION", innerMessage);
        LOGGER.info("FEIGN EXCEPTION | CODE : {} | ERROR_MSG : {}", "FEIGN_EXCEPTION", innerMessage);
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    private String parseFeignException(final FeignException exception) throws JsonProcessingException {
        final String url = exception.request().url();
        final String contented = exception.contentUTF8();
        final Map<String, String> map = objectMapper.readValue(contented, Map.class);
        map.put("request_url", url);
        return map.toString();
    }


    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception exception) {
        final ErrorResponse errorResponse = ErrorResponse.internalServerError(exception.getMessage());
        LOGGER.error("INTERNAL SERVER ERROR OCCURRED | ERROR_MSG : {} | \n {} ", exception.getMessage(), exception.getStackTrace()[0]);
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
