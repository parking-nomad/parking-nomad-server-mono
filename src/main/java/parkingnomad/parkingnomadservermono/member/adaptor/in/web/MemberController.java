package parkingnomad.parkingnomadservermono.member.adaptor.in.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parkingnomad.parkingnomadservermono.config.resolver.AuthMember;
import parkingnomad.parkingnomadservermono.member.application.port.in.member.ResignUseCase;

@RestController
@RequestMapping("api/members")
public class MemberController implements MemberControllerDocs {

    private final ResignUseCase resignUseCase;

    public MemberController(final ResignUseCase resignUseCase) {
        this.resignUseCase = resignUseCase;
    }

    @DeleteMapping
    public ResponseEntity<Void> resign(@AuthMember final Long memberId, final HttpServletResponse response) {
        resignUseCase.resign(memberId);
        final Cookie cookie = new Cookie("refresh_token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
