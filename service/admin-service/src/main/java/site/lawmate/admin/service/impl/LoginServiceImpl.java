package site.lawmate.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.dto.LoginDTO;
import site.lawmate.admin.domain.dto.PrincipalUserDetails;
import site.lawmate.admin.domain.dto.UserModel;
import site.lawmate.admin.domain.vo.ExceptionStatus;
import site.lawmate.admin.domain.vo.Role;
import site.lawmate.admin.handler.LoginException;
import site.lawmate.admin.repository.AdminRepository;
import site.lawmate.admin.service.LoginService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final AdminRepository adminRepository;
    public Mono<PrincipalUserDetails> login(LoginDTO admin) {
        log.info("admin: AuthService {}", admin.getEmail());
        return adminRepository.findByEmail(admin.getEmail())
                .switchIfEmpty(Mono.error(new LoginException(ExceptionStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다.")))
                .flatMap(existingAdmin -> {
                    if (existingAdmin.getPassword().equals(admin.getPassword()) && existingAdmin.getEnabled()) {
                        return Mono.just(new PrincipalUserDetails(UserModel.builder()
                                .id(existingAdmin.getId())
                                .name(existingAdmin.getName())
                                .email(existingAdmin.getEmail())
                                .roles(List.of(Role.ROLE_ADMIN))
                                .build()));
                    } else {
                        return Mono.error(new LoginException(ExceptionStatus.INVALID_PASSWORD, "비밀번호가 일치하지 않습니다."));
                    }
                }).log("adminService login completed {}");
    }
}
