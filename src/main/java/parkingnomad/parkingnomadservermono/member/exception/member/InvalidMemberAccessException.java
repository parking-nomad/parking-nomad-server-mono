package parkingnomad.parkingnomadservermono.member.exception.member;

import parkingnomad.parkingnomadservermono.common.exception.base.ForbiddenException;

public class InvalidMemberAccessException extends ForbiddenException {
    private static final String FIXED_MESSAGE = "현재 로그인한 회원과 조회하려는 회원이 일치하지 않습니다.";

    public InvalidMemberAccessException(final MemberExceptionCode memberExceptionCode) {
        super(memberExceptionCode.getCode(), FIXED_MESSAGE);
    }
}

