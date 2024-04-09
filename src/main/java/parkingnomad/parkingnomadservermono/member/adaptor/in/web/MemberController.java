package parkingnomad.parkingnomadservermono.member.adaptor.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parkingnomad.parkingnomadservermono.config.resolver.AuthMember;
import parkingnomad.parkingnomadservermono.member.application.port.in.member.ResignUseCase;

@RestController
@RequestMapping("api/members")
public class MemberController {

    private final ResignUseCase resignUseCase;

    public MemberController(final ResignUseCase resignUseCase) {
        this.resignUseCase = resignUseCase;
    }

    @DeleteMapping
    public ResponseEntity<Void> resign(@AuthMember final Long memberId) {
        resignUseCase.resign(memberId);
        return ResponseEntity.noContent().build();
    }
}
