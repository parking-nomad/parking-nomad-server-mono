package parkingnomad.parkingnomadservermono.member.application.port.in.member;


import parkingnomad.parkingnomadservermono.member.application.port.in.dto.MemberResponse;

public interface FindMemberUseCase {
    MemberResponse findMember(final Long longMemberId, final Long id);
}
