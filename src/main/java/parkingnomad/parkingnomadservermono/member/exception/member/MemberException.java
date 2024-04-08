package parkingnomad.parkingnomadservermono.member.exception.member;

public abstract class MemberException extends RuntimeException {
    protected final String code;

    protected MemberException(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

