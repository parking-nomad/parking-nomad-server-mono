package parkingnomad.parkingnomadservermono.parking.adaptor.in.web;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.config.resolver.AuthMember;
import parkingnomad.parkingnomadservermono.parking.application.port.in.*;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.PageResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;

import java.net.URI;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/api/parkings")
public class ParkingController implements ParkingControllerDocs {
    private final SaveParkingUseCase saveParkingUseCase;
    private final FindParkingByIdAndMemberIdUseCase findParkingByIdAndMemberIdUseCase;
    private final FindLatestParkingByMemberIdUseCase findLatestParkingByMemberIdUseCase;
    private final DeleteParkingUseCase deleteParkingUseCase;
    private final FindParkingsByMemberIdUseCase findParkingsByMemberIdUseCase;

    public ParkingController(
            final SaveParkingUseCase saveParkingUseCase,
            final FindParkingByIdAndMemberIdUseCase findParkingByIdAndMemberIdUseCase,
            final FindLatestParkingByMemberIdUseCase findLatestParkingByMemberIdUseCase,
            final DeleteParkingUseCase deleteParkingUseCase,
            final FindParkingsByMemberIdUseCase findParkingsByMemberIdUseCase
    ) {
        this.saveParkingUseCase = saveParkingUseCase;
        this.findParkingByIdAndMemberIdUseCase = findParkingByIdAndMemberIdUseCase;
        this.findLatestParkingByMemberIdUseCase = findLatestParkingByMemberIdUseCase;
        this.deleteParkingUseCase = deleteParkingUseCase;
        this.findParkingsByMemberIdUseCase = findParkingsByMemberIdUseCase;
    }

    @PostMapping(consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> saveParking(
            @AuthMember final Long memberId,
            @RequestPart final SaveParkingRequest saveParkingRequest,
            @RequestPart final MultipartFile parkingImage
    ) {
        final Long savedId = saveParkingUseCase.saveParking(memberId, saveParkingRequest, parkingImage);
        return ResponseEntity.created(URI.create("/api/parkings/" + savedId)).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ParkingResponse> findParkingById(
            @PathVariable final Long id,
            @AuthMember final Long memberId
    ) {
        final ParkingResponse response = findParkingByIdAndMemberIdUseCase.findParkingByIdAndMemberId(id, memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/latest")
    public ResponseEntity<ParkingResponse> findLatestParking(@AuthMember final Long memberId) {
        final ParkingResponse response = findLatestParkingByMemberIdUseCase.findLatestParkingByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteParkingById(@PathVariable final Long id, @AuthMember final Long memberId) {
        deleteParkingUseCase.deleteParking(memberId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<ParkingResponse>> findParkingsByPage(
            final Pageable pageable,
            @AuthMember final Long memberId
    ) {
        final PageResponse<ParkingResponse> response = findParkingsByMemberIdUseCase.findParkingsByMemberId(pageable, memberId);
        return ResponseEntity.ok(response);
    }
}
