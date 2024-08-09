package site.lawmate.lawyer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.dto.LoginDto;
import site.lawmate.lawyer.domain.dto.PrincipalUserDetails;
import site.lawmate.lawyer.service.LoginService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final LoginService loginService;

    @PostMapping("/login")
    public Mono<PrincipalUserDetails> login(@RequestBody LoginDto lawyer) {
        log.info("admin: {}", lawyer.getEmail());
        return loginService.login(lawyer);
    }
}
