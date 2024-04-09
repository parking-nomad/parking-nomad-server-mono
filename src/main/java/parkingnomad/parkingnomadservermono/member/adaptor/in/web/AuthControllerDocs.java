package parkingnomad.parkingnomadservermono.member.adaptor.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import parkingnomad.parkingnomadservermono.common.dto.ErrorResponse;
import parkingnomad.parkingnomadservermono.member.adaptor.in.web.dto.AccessTokenResponse;
import parkingnomad.parkingnomadservermono.member.domain.oauth.provider.OAuthProvider;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Auth API", description = "인증 & 인가를 관리하는 API")
public interface AuthControllerDocs {

    @Operation(summary = "소셜 로그인을 통한 토큰 발급 API", description = "code와 provider를 통해 토큰을 발급한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 발급 성공",
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "refresh token을 쿠키에 저장한다. key = refresh_token, Max-Age = 604800(일주일)"
                    ),
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 provider로 인한 토큰 발급 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 소셜로그인 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<AccessTokenResponse> loginWithSocial(
            @Parameter(description = "provider로 부터 발급받는 인가 코드") final String code,
            @Parameter(description = "소셜로그인 provider 명") final OAuthProvider oAuthProvider,
            @Parameter(hidden = true) final HttpServletResponse response
    );

    @Operation(summary = "토큰 재발급 API", description = "쿠키에 저장된 리프레시 토큰을 통해 토큰을 재발급한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "새로운 refresh token을 쿠키에 저장한다. key = refresh_token, Max-Age = 604800(일주일)"
                    ),
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AccessTokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "리프레시 토큰에 해당하는 멤버가 존재하지 않아 토큰 재발급 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "잘못된 refresh_token으로 인한 토큰 재발급 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 토큰 재발급 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<AccessTokenResponse> refreshTokens(
            @Parameter(description = "리프레시 토큰, 쿠키를 통해 전달") final String refreshToken,
            @Parameter(hidden = true) final HttpServletResponse response
    );

    @Operation(summary = "로그아웃 API", description = "로그인 된 리프레시 토큰을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "로그아웃 성공",
                    headers = @Header(
                            name = "Set-Cookie",
                            description = "쿠키에 저장된 refresh token을 삭제한다. key = refresh_token, Max-Age = 0"
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 에러로 인한 로그아웃 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> logout(
            @Parameter(description = "리프레시 토큰, 쿠키를 통해 전달") final String refreshToken,
            @Parameter(hidden = true) final HttpServletResponse response
    );
}
