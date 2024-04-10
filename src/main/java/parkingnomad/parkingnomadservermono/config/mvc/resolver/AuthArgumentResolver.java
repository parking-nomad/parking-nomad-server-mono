package parkingnomad.parkingnomadservermono.config.mvc.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidAccessTokenException;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidTokenFormatException;
import parkingnomad.parkingnomadservermono.common.jwt.TokenParser;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;

import java.util.Objects;

import static parkingnomad.parkingnomadservermono.common.exception.auth.AuthExceptionCode.INVALID_ACCESS_TOKEN;
import static parkingnomad.parkingnomadservermono.common.exception.auth.AuthExceptionCode.INVALID_TOKEN_FORMAT;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthArgumentResolver.class);
    private static final String AUTHORIZATION = "Authorization";
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private static final String MEMBER_ID = "MEMBER_ID";

    private final TokenParser tokenParser;
    private final MemberRepository memberRepository;

    public AuthArgumentResolver(
            final TokenParser tokenParser,
            final MemberRepository memberRepository
    ) {
        this.tokenParser = tokenParser;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final String accessToken = extractAccessTokenFromRequest(webRequest);
        final Long memberId = tokenParser.parseToMemberIdFromAccessToken(accessToken);
        final boolean isExisted = memberRepository.isExistedMember(memberId);
        if (!isExisted) {
            throw new InvalidAccessTokenException(INVALID_ACCESS_TOKEN.getCode());
        }
        MDC.put(MEMBER_ID, String.valueOf(memberId));
        LOGGER.info("PASS AUTHORIZATION");
        return memberId;
    }

    private String extractAccessTokenFromRequest(final NativeWebRequest webRequest) {
        final String authorization = webRequest.getHeader(AUTHORIZATION);
        if (Objects.isNull(authorization) || !authorization.startsWith(ACCESS_TOKEN_PREFIX)) {
            throw new InvalidTokenFormatException(INVALID_TOKEN_FORMAT.getCode());
        }
        return authorization.replace(ACCESS_TOKEN_PREFIX, "");
    }
}
