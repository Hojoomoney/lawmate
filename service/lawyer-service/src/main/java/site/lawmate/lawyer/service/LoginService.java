package site.lawmate.lawyer.service;

import reactor.core.publisher.Mono;
import site.lawmate.lawyer.domain.dto.LoginDto;
import site.lawmate.lawyer.domain.dto.PrincipalUserDetails;


public interface LoginService {

    Mono<PrincipalUserDetails> login(LoginDto admin);

}
