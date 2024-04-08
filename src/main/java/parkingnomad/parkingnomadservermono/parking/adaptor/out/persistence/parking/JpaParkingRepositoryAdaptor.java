package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaParkingRepositoryAdaptor extends JpaRepository<JpaParkingEntity, Long> {
    Optional<JpaParkingEntity> findJpaParkingEntityByIdAndMemberId(final Long id, final Long memberId);

    Optional<JpaParkingEntity> findFirstByMemberIdOrderByCreatedAtDesc(final Long memberId);

    void deleteByIdAndId(final Long memberId, final Long id);
}
