package parkingnomad.parkingnomadservermono.parking.adaptor.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parkingnomad.parkingnomadservermono.config.resolver.AuthMember;
import parkingnomad.parkingnomadservermono.parking.application.port.in.DeleteParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.FindLatestParkingByMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.FindParkingByIdAndMemberIdUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.SaveParkingUseCase;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.ParkingResponse;
import parkingnomad.parkingnomadservermono.parking.application.port.in.dto.SaveParkingRequest;

import java.net.URI;

@RestController
@RequestMapping("/api/parkings")
public class ParkingController {
    private final SaveParkingUseCase saveParkingUseCase;
    private final FindParkingByIdAndMemberIdUseCase findParkingByIdAndMemberIdUseCase;
    private final FindLatestParkingByMemberIdUseCase findLatestParkingByMemberIdUseCase;
    private final DeleteParkingUseCase deleteParkingUseCase;

    public ParkingController(
            final SaveParkingUseCase saveParkingUseCase,
            final FindParkingByIdAndMemberIdUseCase findParkingByIdAndMemberIdUseCase,
            final FindLatestParkingByMemberIdUseCase findLatestParkingByMemberIdUseCase,
            final DeleteParkingUseCase deleteParkingUseCase
    ) {
        this.saveParkingUseCase = saveParkingUseCase;
        this.findParkingByIdAndMemberIdUseCase = findParkingByIdAndMemberIdUseCase;
        this.findLatestParkingByMemberIdUseCase = findLatestParkingByMemberIdUseCase;
        this.deleteParkingUseCase = deleteParkingUseCase;
    }

    @PostMapping
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
}
