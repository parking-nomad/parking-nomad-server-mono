package parkingnomad.parkingnomadservermono.parking.adaptor.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Parking API", description = "주차 정보를 관리하는 API")
public interface ParkingControllerDocs {

    @Operation(summary = "주차정보 등록", description = "주차 정보를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "주차정보 등록 성공",
                    headers = {@Header(name = "Location", description = "저장 된 주차정보를 조회할 수 있는 API")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력으로 인한, 주차정보 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 || 인가 실패로 인한, 주차정보 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 주차 정보 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> saveParking(
            @Parameter(hidden = true) final Long memberId,
            @Parameter final SaveParkingRequest saveParkingRequest,
            @Parameter final MultipartFile parkingImage
    );

    @Operation(summary = "주차정보 id를 통한 주차정보 조회", description = "로그인 된 회원의 주차 정보를 조회 한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주차정보 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParkingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력으로 인한 주차정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 || 인가 실패로 인한 주차정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 주차 정보 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<ParkingResponse> findParkingById(
            @Parameter(description = "조회 하려는 주차정보의 id") final Long id,
            @Parameter(hidden = true) final Long memberId
    );

    @Operation(summary = "최근 주차 정보 조회", description = "로그인 된 회원의 최근 주차 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주차정보 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ParkingResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력으로 인한 주차정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 || 인가 실패로 인한 주차정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 주차 정보 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<ParkingResponse> findLatestParking(@Parameter(hidden = true) final Long memberId);

    @Operation(summary = "주차 정보 삭제", description = "주차 정보 id를 통해 로그인 된 회원의 주차 정보를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "주차정보 삭제 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력으로 인한 주차 정보 삭제 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 || 인가 실패로 인한 최근 주차 정보 삭제 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 최근 주차 정보 삭제 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> deleteParkingById(
            @Parameter(description = "삭제하려는 주차정보의 id") final Long id,
            @Parameter(hidden = true) final Long memberId
    );

    @Operation(summary = "주차 정보 목록 조회", description = "로그인 된 회원의 주차 정보 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "주차정보 목록 조회 성공",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 입력으로 인한 주차 정보 목록 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 || 인가 실패로 인한 최근 주차 정보 목록 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 최근 주차 정보 목록 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작"),
            @Parameter(name = "size", description = "페이지의 사이즈, 한 페이지에 담길 contents 갯수")
    })
    ResponseEntity<PageResponse<ParkingResponse>> findParkingsByPage(@Parameter(hidden = true) final Pageable pageable, @Parameter(hidden = true) final Long memberId);
}
