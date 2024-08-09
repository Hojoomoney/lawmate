package site.lawmate.admin.service;

import reactor.core.publisher.Mono;
import site.lawmate.admin.domain.dto.LoginDTO;
import site.lawmate.admin.domain.dto.PrincipalUserDetails;


public interface LoginService {

    Mono<PrincipalUserDetails> login(LoginDTO admin);

}
