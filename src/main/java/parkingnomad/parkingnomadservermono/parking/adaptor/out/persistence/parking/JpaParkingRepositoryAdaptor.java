package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaParkingRepositoryAdaptor extends JpaRepository<JpaParkingEntity, Long> {
    Optional<JpaParkingEntity> findJpaParkingEntityByIdAndMemberId(final Long id, final Long memberId);

    Optional<JpaParkingEntity> findFirstByMemberIdOrderByCreatedAtDesc(final Long memberId);

    Slice<JpaParkingEntity> findJpaParkingEntityByMemberIdOrderByCreatedAtDesc(final Pageable pageable, final Long memberId);

    void deleteAllByMemberId(final Long memberId);
}
