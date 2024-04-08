package parkingnomad.parkingnomadservermono.member.application.port.in;

public interface LogoutUseCase {
    void logout(final String refreshToken);
}
