package site.lawmate.lawyer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.dto.LoginDto;
import site.lawmate.lawyer.domain.dto.PrincipalUserDetails;
import site.lawmate.lawyer.domain.dto.UserModel;
import site.lawmate.lawyer.domain.vo.ExceptionStatus;
import site.lawmate.lawyer.domain.vo.Role;
import site.lawmate.lawyer.handler.LoginException;
import site.lawmate.lawyer.repository.LawyerRepository;
import site.lawmate.lawyer.service.LoginService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private final LawyerRepository lawyerRepository;
    @Override
    public Mono<PrincipalUserDetails> login(LoginDto lawyer) {
        log.info("Lawyer : AuthService {}", lawyer.getEmail());
        return lawyerRepository.findByEmail(lawyer.getEmail())
                .switchIfEmpty(Mono.error(new LoginException(ExceptionStatus.UNAUTHORIZED, "존재하지 않는 사용자이거나 틀린 비밀번호 입니다.")))
                .flatMap(existingLawyer -> {
                    if (existingLawyer.getPassword().equals(lawyer.getPassword()))
                        return Mono.just(new PrincipalUserDetails(UserModel.builder()
                                .id(existingLawyer.getId())
                                .name(existingLawyer.getName())
                                .email(existingLawyer.getEmail())
                                .roles(List.of(Role.ROLE_LAWYER))
                                .build()));
                    else {
                        return Mono.error(new LoginException(ExceptionStatus.INVALID_PASSWORD, "존재하지 않는 사용자이거나 틀린 비밀번호 입니다."));
                    }
                });
    }
}
