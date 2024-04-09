package parkingnomad.parkingnomadservermono.member.adaptor.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface MemberControllerDocs {

    @Operation(summary = "회원탈퇴 API", description = "탈퇴된 회원의 주차정보는 삭제된다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "회원 탈퇴 성공",
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "refresh token을 삭제한다. key = refresh_token, Max-Age = 0"
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 회원탈퇴 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> resign(
            @Parameter(hidden = true) final Long memberId,
            @Parameter(hidden = true) final HttpServletResponse response
    );
}
