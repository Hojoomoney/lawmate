package site.lawmate.gateway.filter;

import java.util.List;
import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import site.lawmate.gateway.domain.vo.ExceptionStatus;
import site.lawmate.gateway.domain.vo.Role;
import site.lawmate.gateway.exception.GatewayException;
import site.lawmate.gateway.provider.JwtTokenProvider;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> { //게이트웨이에서 filter를 생산
    private final JwtTokenProvider jwtTokenProvider;

    public AuthorizationHeaderFilter(JwtTokenProvider jwtTokenProvider){
        super(Config.class);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Data
    public static class Config {
        private String headerName;
        private String headerValue;
        private List<Role> roles;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> //exchange : 요청, 응답을 가지고 있는 객체, chain : 다음 필터로 요청을 넘기는 역할
                Mono.just(exchange)
                        .flatMap(i -> Mono.just(Objects.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION))))
                        .flatMap(i -> Mono.just(i.get(0)))
                        .switchIfEmpty(Mono.error(new GatewayException(ExceptionStatus.UNAUTHORIZED,"No Authorization Header")))
                        .filterWhen(i -> Mono.just(i.startsWith("Bearer ")))
                        .flatMap(i -> Mono.just(jwtTokenProvider.removeBearer(i)))
                        .filterWhen(i -> Mono.just(jwtTokenProvider.isTokenValid(i, false)))
                        .switchIfEmpty(Mono.error(new GatewayException(ExceptionStatus.UNAUTHORIZED,"Invalid Token")))
                        .flatMapMany(i -> Flux.just(jwtTokenProvider.extractRoles(i).stream().map(Role::valueOf).findAny().orElseGet(() -> Role.ROLE_GUEST)))
                        .any(config.getRoles()::contains)
                        .filter(i -> i)
                        .switchIfEmpty(Mono.error(new GatewayException(ExceptionStatus.NO_PERMISSION, "No Permission")))
                        .flatMap(i -> chain.filter(exchange))
                        .onErrorResume(GatewayException.class, e -> onError(exchange, HttpStatusCode.valueOf(e.getStatus().getStatus().value()), e.getMessage()))
                        .log()
        );
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatusCode httpStatusCode, String message){
        log.error("Error Occured : {}, {}, {}", exchange.getRequest().getURI(), httpStatusCode, message);
        exchange.getResponse().setStatusCode(httpStatusCode);
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(message.getBytes())));
    }

}