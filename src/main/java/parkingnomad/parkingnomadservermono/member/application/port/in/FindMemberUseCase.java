package parkingnomad.parkingnomadservermono.member.application.port.in;


import parkingnomad.parkingnomadservermono.member.application.port.in.dto.MemberResponse;

public interface FindMemberUseCase {
    MemberResponse findMember(final Long longMemberId, final Long id);
}
