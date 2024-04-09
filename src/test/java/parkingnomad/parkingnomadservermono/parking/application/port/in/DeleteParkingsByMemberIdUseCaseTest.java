package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking.JpaParkingEntity;
import parkingnomad.parkingnomadservermono.parking.adaptor.out.persistence.parking.JpaParkingRepositoryAdaptor;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteParkingsByMemberIdUseCaseTest extends BaseTestWithContainers {

    @Autowired
    ParkingRepository repository;

    @Autowired
    JpaParkingRepositoryAdaptor adaptor;

    @Autowired
    DeleteParkingsByMemberIdUseCase useCase;

    @Test
    @DisplayName("멤버id가 일치하는 주차정보를 모두 삭제한다.")
    void deleteByMemberId() {
        //given
        final long memberId = 1L;
        repository.save(Parking.createWithoutId(2L, 20, 30, "address2"));
        repository.save(Parking.createWithoutId(memberId, 20, 30, "address1"));
        repository.save(Parking.createWithoutId(memberId, 20, 30, "address3"));
        repository.save(Parking.createWithoutId(memberId, 20, 30, "address5"));
        repository.save(Parking.createWithoutId(2L, 20, 30, "address4"));
        repository.save(Parking.createWithoutId(2L, 20, 30, "address6"));

        //when
        useCase.deleteParkingsByMemberIdUseCase(memberId);

        //then
        final List<JpaParkingEntity> all = adaptor.findAll();
        assertThat(all).hasSize(3);
    }

}
