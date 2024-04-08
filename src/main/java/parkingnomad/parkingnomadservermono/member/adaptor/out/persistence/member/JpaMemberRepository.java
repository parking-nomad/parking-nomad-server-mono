package parkingnomad.parkingnomadservermono.member.adaptor.out.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<JpaMemberEntity, Long> {
    Optional<JpaMemberEntity> findBySub(final String sub);
}
