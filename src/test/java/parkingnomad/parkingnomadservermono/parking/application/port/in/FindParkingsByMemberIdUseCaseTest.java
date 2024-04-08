package parkingnomad.parkingnomadservermono.parking.application.port.in;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.out.persistence.ParkingRepository;
import parkingnomad.parkingnomadservermono.parking.domain.Parking;
import parkingnomad.parkingnomadservermono.support.BaseTestWithContainers;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class FindParkingsByMemberIdUseCaseTest extends BaseTestWithContainers {

    @Autowired
    FindParkingsByMemberIdUseCase useCase;

    @Autowired
    ParkingRepository repository;

    @Test
    @DisplayName("memberId가 일치하는 주차정보를 Pageable에 맞게 반환한다. ")
    void findParkingByMemberId() {
        //given
        final long memberId = 1L;
        repository.save(Parking.createWithoutId(memberId, 0, 10, "address1"));
        repository.save(Parking.createWithoutId(memberId, 10, 20, "address2"));
        repository.save(Parking.createWithoutId(memberId, 20, 30, "address3"));
        final Parking target1 = repository.save(Parking.createWithoutId(memberId, 30, 40, "address4"));
        final Parking target2 = repository.save(Parking.createWithoutId(memberId, 40, 50, "address5"));
        final Parking target3 = repository.save(Parking.createWithoutId(memberId, 50, 60, "address6"));
        final int pageNumber = 0;
        final int pageSize = 3;
        final PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        //when
        final PageResponse<ParkingResponse> results = useCase.findParkingsByMemberId(pageRequest, memberId);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(results.getHasNext()).isTrue();
            softAssertions.assertThat(results.getCurrentPage()).isEqualTo(pageNumber);
            softAssertions.assertThat(results.getContentsSize()).isEqualTo(3);
            softAssertions.assertThat(results.getContents())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(
                            ParkingResponse.from(target3),
                            ParkingResponse.from(target2),
                            ParkingResponse.from(target1)
                    );
        });
    }

}
