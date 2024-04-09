package parkingnomad.parkingnomadservermono.member.adaptor.out.persistence.member;

import org.springframework.stereotype.Component;
import parkingnomad.parkingnomadservermono.member.application.port.out.persistence.MemberRepository;
import parkingnomad.parkingnomadservermono.member.domain.Member;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
public class JpaMemberAdaptor implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;
    private final JpaMemberMapper jpaMemberMapper;

    public JpaMemberAdaptor(
            final JpaMemberRepository jpaMemberRepository,
            final JpaMemberMapper jpaMemberMapper
    ) {
        this.jpaMemberRepository = jpaMemberRepository;
        this.jpaMemberMapper = jpaMemberMapper;
    }

    @Override
    public Member saveMember(final Member member) {
        final JpaMemberEntity jpaMemberEntity = jpaMemberMapper.toJpaMemberEntity(member);
        jpaMemberRepository.save(jpaMemberEntity);
        return jpaMemberMapper.toMemberDomainEntity(jpaMemberEntity);
    }

    @Override
    public Optional<Member> findById(final Long id) {
        final JpaMemberEntity jpaMemberEntity = jpaMemberRepository.findById(id)
                .orElse(null);
        if (Objects.isNull(jpaMemberEntity)) {
            return Optional.empty();
        }
        final Member member = jpaMemberMapper.toMemberDomainEntity(jpaMemberEntity);
        return Optional.of(member);
    }

    @Override
    public Optional<Member> findBySub(final String sub) {
        final JpaMemberEntity jpaMemberEntity = jpaMemberRepository.findBySub(sub)
                .orElse(null);
        if (isNull(jpaMemberEntity)) {
            return Optional.empty();
        }
        return Optional.of(jpaMemberMapper.toMemberDomainEntity(jpaMemberEntity));
    }

    @Override
    public boolean isExistedMember(final Long id) {
        return jpaMemberRepository.existsById(id);
    }

    @Override
    public void deleteById(final Long id) {
        jpaMemberRepository.deleteById(id);
    }
}
