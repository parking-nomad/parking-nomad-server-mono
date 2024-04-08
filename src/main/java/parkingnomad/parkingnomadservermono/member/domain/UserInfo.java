package parkingnomad.parkingnomadservermono.member.domain;

import parkingnomad.parkingnomadservermono.member.exception.oauth.InvalidOpenIdException;

import static java.util.Objects.isNull;

public class UserInfo {
    private final String sub;
    private final String provider;
    private final String nickname;

    private UserInfo(final String sub, final String provider, final String nickname) {
        this.sub = sub;
        this.provider = provider;
        this.nickname = nickname;
    }

    public static UserInfo of(final String sub, final String provider, final String nickname) {
        if (isNull(sub)) {
            throw new InvalidOpenIdException(provider);
        }

        if (isNull(provider)) {
            throw new InvalidOpenIdException(provider);
        }

        if (isNull(nickname)) {
            throw new InvalidOpenIdException(provider);
        }
        return new UserInfo(sub, provider, nickname);
    }

    public String getSubWithProvider() {
        return provider + sub;
    }

    public String getSub() {
        return sub;
    }

    public String getProvider() {
        return provider;
    }

    public String getNickname() {
        return nickname;
    }
}

