package parkingnomad.parkingnomadservermono.member.application.port.out.persistence;


import parkingnomad.parkingnomadservermono.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member saveMember(final Member member);

    Optional<Member> findById(final Long id);

    Optional<Member> findBySub(final String sub);

    boolean isExistedMember(final Long id);
}
