package parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class JpaParkingRepository implements ParkingRepository {

    private final JpaParkingMapper mapper;
    private final JpaParkingRepositoryAdaptor parkings;

    public JpaParkingRepository(final JpaParkingMapper mapper, final JpaParkingRepositoryAdaptor parkings) {
        this.mapper = mapper;
        this.parkings = parkings;
    }

    @Override
    public Parking save(final Parking parking) {
        final JpaParkingEntity jpaEntity = mapper.toJpaEntity(parking);
        final JpaParkingEntity saved = parkings.save(jpaEntity);
        return mapper.toDomainEntity(saved);
    }

    @Override
    public Optional<Parking> findById(final Long id) {
        final JpaParkingEntity jpaParkingEntity = parkings.findById(id)
                .orElse(null);
        if (isNull(jpaParkingEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomainEntity(jpaParkingEntity));
    }

    @Override
    public Optional<Parking> findByIdAndMemberId(final Long id, final Long memberId) {
        final JpaParkingEntity jpaParkingEntity = parkings.findJpaParkingEntityByIdAndMemberId(id, memberId)
                .orElse(null);
        if (isNull(jpaParkingEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomainEntity(jpaParkingEntity));
    }

    @Override
    public Optional<Parking> findLatestParkingByMemberId(final Long memberId) {
        final JpaParkingEntity jpaParkingEntity = parkings.findFirstByMemberIdOrderByCreatedAtDesc(memberId).orElse(null);
        if (isNull(jpaParkingEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomainEntity(jpaParkingEntity));
    }

    @Override
    public void deleteById(final Long id) {
        parkings.deleteById(id);
    }

    @Override
    public Slice<Parking> findParkingsByMemberIdAndPage(final Pageable pageable, final Long memberId) {
        final Slice<JpaParkingEntity> results = parkings.findJpaParkingEntityByMemberIdOrderByCreatedAtDesc(pageable, memberId);
        final List<Parking> contents = results.getContent().stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
        return new SliceImpl<>(contents, pageable, results.hasNext());
    }

    @Override
    @Transactional
    public void deleteParkingsByMemberId(final Long memberId) {
        parkings.deleteAllByMemberId(memberId);
    }
}
