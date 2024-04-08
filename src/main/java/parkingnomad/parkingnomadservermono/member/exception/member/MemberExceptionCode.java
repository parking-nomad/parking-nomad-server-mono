package parkingnomad.parkingnomadservermono.member.exception.member;

public enum MemberExceptionCode {
    NON_EXISTENT_MEMBER("MEMBER0001"),
    INVALID_MEMBER_ACCESS_EXCEPTION("MEMBER0002");

    private final String code;

    MemberExceptionCode(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

