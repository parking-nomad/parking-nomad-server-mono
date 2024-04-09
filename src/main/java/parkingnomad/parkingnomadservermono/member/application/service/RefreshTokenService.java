package parkingnomad.parkingnomadservermono.member.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.member.application.port.in.auth.RefreshTokensUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.TokenResponse;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.RefreshTokenRepository;
import parkingnomad.parkingnomadservermono.common.jwt.TokenParser;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidRefreshTokenException;
import parkingnomad.parkingnomadservermono.member.exception.member.NonExistentMemberException;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static parkingnomad.parkingnomadservermono.common.exception.auth.AuthExceptionCode.INVALID_REFRESH_TOKEN;
import static parkingnomad.parkingnomadservermono.member.exception.member.MemberExceptionCode.NON_EXISTENT_MEMBER;

@Service
@Transactional(readOnly = true)
public class RefreshTokenService implements RefreshTokensUseCase {

    private final TokenParser tokenParser;
    private final RefreshTokenRepository refreshTokenAdaptor;
    private final MemberRepository memberRepository;

    public RefreshTokenService(
            final TokenParser tokenParser,
            final RefreshTokenRepository refreshTokenAdaptor,
            final MemberRepository memberRepository
    ) {
        this.tokenParser = tokenParser;
        this.refreshTokenAdaptor = refreshTokenAdaptor;
        this.memberRepository = memberRepository;
    }

    @Override
    public TokenResponse refreshTokens(final String refreshToken) {
        if (isBlank(refreshToken)) {
            throw new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN.getCode());
        }

        final Long memberId = refreshTokenAdaptor.findMemberIdByRefreshToken(refreshToken)
                .orElseThrow(() -> new InvalidRefreshTokenException(INVALID_REFRESH_TOKEN.getCode()));
        System.out.println(refreshToken);
        System.out.println(memberId);
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NonExistentMemberException(NON_EXISTENT_MEMBER.getCode(), memberId));

        final String accessToken = tokenParser.parseToAccessTokenFrom(memberId);
        final String newRefreshToken = tokenParser.createRefreshToken();
        refreshTokenAdaptor.save(newRefreshToken, memberId);
        refreshTokenAdaptor.deleteByRefreshToken(refreshToken);

        return new TokenResponse(accessToken, newRefreshToken);
    }
}
