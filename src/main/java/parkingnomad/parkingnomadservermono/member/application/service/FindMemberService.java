package parkingnomad.parkingnomadservermono.member.application.service;


import org.springframework.stereotype.Service;
import parkingnomad.parkingnomadservermono.member.application.port.in.FindMemberUseCase;
import parkingnomad.parkingnomadservermono.member.application.port.in.dto.MemberResponse;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.domain.Member;
import parkingnomad.parkingnomadservermono.member.exception.member.InvalidMemberAccessException;
import parkingnomad.parkingnomadservermono.member.exception.member.NonExistentMemberException;

import java.util.Objects;

import static parkingnomad.parkingnomadservermono.member.exception.member.MemberExceptionCode.INVALID_MEMBER_ACCESS_EXCEPTION;
import static parkingnomad.parkingnomadservermono.member.exception.member.MemberExceptionCode.NON_EXISTENT_MEMBER;

@Service
public class FindMemberService implements FindMemberUseCase {

    private final MemberRepository memberRepository;

    public FindMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberResponse findMember(final Long loginMemberId, final Long id) {
        if (!Objects.equals(loginMemberId, id)) {
            throw new InvalidMemberAccessException(INVALID_MEMBER_ACCESS_EXCEPTION);
        }
        final Member member = memberRepository.findById(id)
                .orElseThrow(() -> new NonExistentMemberException(NON_EXISTENT_MEMBER.getCode(), id));
        return new MemberResponse(member.getId(), member.getName());
    }
}
