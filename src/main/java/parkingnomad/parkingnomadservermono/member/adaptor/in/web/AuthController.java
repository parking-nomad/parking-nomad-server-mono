package parkingnomad.parkingnomadservermono.member.adaptor.in.web;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import parkingnomad.parkingnomadservermono.member.adaptor.in.web.dto.AccessTokenResponse;
import parkingnomad.parkingnomadservermono.member.application.port.in.auth.LogoutUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.in.auth.RefreshTokensUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.in.auth.SocialLoginUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;
import parkingnomad.parkingnomadservermono.member.domain.oauth.provider.OAuthProvider;

@Controller
@RequestMapping("api/auth")
public class AuthController implements AuthControllerDocs {

    public static final String REFRESH_TOKEN = "refresh_token";
    public static final int MAX_AGE = 60 * 60 * 24 * 7;
    private final SocialLoginUseCase socialLoginUseCase;
    private final RefreshTokensUseCase refreshTokensUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(
            final SocialLoginUseCase socialLoginUseCase,
            final RefreshTokensUseCase refreshTokensUseCase,
            final LogoutUseCase logoutUseCase
    ) {
        this.socialLoginUseCase = socialLoginUseCase;
        this.refreshTokensUseCase = refreshTokensUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @GetMapping("{provider}")
    public ResponseEntity<AccessTokenResponse> loginWithSocial(
            @RequestParam final String code,
            @PathVariable("provider") final OAuthProvider oAuthProvider,
            final HttpServletResponse response
    ) {
        final TokenResponse tokenResponse = socialLoginUseCase.socialLogin(code, oAuthProvider);
        final Cookie cookie = getRefreshTokenCookie(tokenResponse.refreshToken(), MAX_AGE);
        response.addCookie(cookie);
        return ResponseEntity.ok(new AccessTokenResponse(tokenResponse.accessToken()));
    }

    private Cookie getRefreshTokenCookie(final String refreshToken, final int maxAge) {
        final Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setMaxAge(MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    @GetMapping("refresh")
    public ResponseEntity<AccessTokenResponse> refreshTokens(
            @CookieValue(name = REFRESH_TOKEN) final String refreshToken,
            final HttpServletResponse response
    ) {
        final TokenResponse tokenResponse = refreshTokensUseCase.refreshTokens(refreshToken);
        final Cookie cookie = getRefreshTokenCookie(tokenResponse.refreshToken(), MAX_AGE);
        response.addCookie(cookie);
        return ResponseEntity.ok(new AccessTokenResponse(tokenResponse.accessToken()));
    }

    @GetMapping("logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_TOKEN) final String refreshToken,
            final HttpServletResponse response
    ) {
        logoutUseCase.logout(refreshToken);
        final Cookie refreshTokenCookie = getRefreshTokenCookie("", 0);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.noContent().build();
    }
}
