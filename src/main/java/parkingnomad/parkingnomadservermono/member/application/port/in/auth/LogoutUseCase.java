package parkingnomad.parkingnomadservermono.member.application.port.in.auth;

public interface LogoutUseCase {
    void logout(final String refreshToken);
}
