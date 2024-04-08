package parkingnomad.parkingnomadservermono.member.application.port.in;


import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;
import parkingnomad.parkingnomadservermono.member.domain.oauth.provider.OAuthProvider;

public interface SocialLoginUseCase {
    TokenResponse socialLogin(String code, OAuthProvider oAuthProvider);
}
