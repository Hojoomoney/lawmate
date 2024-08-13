package site.lawmate.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import site.lawmate.gateway.domain.dto.MessengerDTO;
import site.lawmate.gateway.domain.model.PrincipalUserDetails;
import site.lawmate.gateway.provider.JwtTokenProvider;

import java.net.URI;

@Log4j2
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("::::::webFilterExchange 정보: "+webFilterExchange);
        log.info("::::::authentication 정보: "+authentication);
        log.info("::::::getAuthorities 정보: "+authentication.getAuthorities());
        log.info("::::::getCredentials 정보: "+authentication.getCredentials());
        webFilterExchange.getExchange().getResponse().setStatusCode(HttpStatus.FOUND);
        authentication.getAuthorities().stream().filter(i -> i.getAuthority().equals("ROLE_USER")).findAny()
                .ifPresentOrElse(
                        i -> webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create("https://localhost:3000")),
                        () -> webFilterExchange.getExchange().getResponse().getHeaders().setLocation(URI.create("https://localhost:3000/detail"))
                );
        webFilterExchange.getExchange().getResponse().getHeaders().add("Content-Type", "application/json");
        return webFilterExchange.getExchange().getResponse()
                .writeWith(
                        jwtTokenProvider.generateToken((PrincipalUserDetails)authentication.getPrincipal(), false)
                                .doOnNext(accessToken ->
                                        webFilterExchange
                                                .getExchange()
                                                .getResponse()
                                                .getCookies()
                                                .add("accessToken",
                                                        ResponseCookie.from("accessToken", accessToken)
                                                                .path("/")
                                                                .maxAge(jwtTokenProvider.getAccessTokenExpired())
                                                                .domain(".lawmate.site")
                                                                .secure(true)
                                                                .httpOnly(true)
                                                                .build()
                                                )
                                )
                                .flatMap(i -> jwtTokenProvider.generateToken((PrincipalUserDetails)authentication.getPrincipal(), true))
                                .doOnNext(refreshToken ->
                                        webFilterExchange
                                                .getExchange()
                                                .getResponse()
                                                .getCookies()
                                                .add("refreshToken",
                                                        ResponseCookie.from("refreshToken", refreshToken)
                                                                .path("/")
                                                                .maxAge(jwtTokenProvider.getRefreshTokenExpired())
                                                                .domain(".lawmate.site")
                                                                .secure(true)
                                                                .httpOnly(true)
                                                                .build()
                                                )
                                )
                                .flatMap(i ->
                                        Mono.just(
                                                MessengerDTO.builder()
                                                        .message("로그인 성공")
                                                        .build()
                                        )
                                )
                                .flatMap(messageDTO ->
                                        Mono.just(
                                                webFilterExchange.getExchange()
                                                        .getResponse()
                                                        .bufferFactory()
                                                        .wrap(writeValueAsBytes(messageDTO))
                                        )
                                )
                );
    }


    private byte[] writeValueAsBytes(MessengerDTO messengerDTO) {
        try {
            return objectMapper.writeValueAsBytes(messengerDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
