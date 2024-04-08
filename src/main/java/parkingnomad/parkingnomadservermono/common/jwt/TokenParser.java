package parkingnomad.parkingnomadservermono.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.common.exception.auth.ExpiredAccessTokenException;
import parkingnomad.parkingnomadservermono.common.exception.auth.InvalidAccessTokenException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import static parkingnomad.parkingnomadservermono.common.exception.auth.AuthExceptionCode.EXPIRED_ACCESS_TOKEN;
import static parkingnomad.parkingnomadservermono.common.exception.auth.AuthExceptionCode.INVALID_ACCESS_TOKEN;


@Component
public class TokenParser {
    public static final String UUID_DELIMITER = "-";
    private final SecretKey key;
    private final long accessTokenExpired;

    public TokenParser(@Value("${spring.auth.key}") final String key,
                       @Value("${spring.auth.access_token_expired}") final long accessTokenExpired
    ) {
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpired = accessTokenExpired;
    }

    public String parseToAccessTokenFrom(final Long memberId) {
        return builder(accessTokenExpired)
                .setSubject(String.valueOf(memberId))
                .compact();
    }

    public Long parseToMemberIdFromAccessToken(final String accessToken) {
        try {
            final String subject = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody()
                    .getSubject();
            return Long.parseLong(subject);
        } catch (ExpiredJwtException exception) {
            throw new ExpiredAccessTokenException(EXPIRED_ACCESS_TOKEN.getCode());
        } catch (SignatureException exception) {
            throw new InvalidAccessTokenException(INVALID_ACCESS_TOKEN.getCode());
        }
    }

    private JwtBuilder builder(final long expired) {
        final Date validity = new Date(System.currentTimeMillis() + expired);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256);
    }

    public String createRefreshToken() {
        return String.join("", UUID.randomUUID().toString().split(UUID_DELIMITER));
    }
}
