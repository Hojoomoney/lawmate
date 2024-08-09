package site.lawmate.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import site.lawmate.gateway.domain.dto.LoginDTO;
import site.lawmate.gateway.handler.AuthHandler;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthRouter {
    private final AuthHandler authHandler;

    @Bean
    RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions.route()
                .POST("/auth/login/local", req -> req.bodyToMono(LoginDTO.class).flatMap(authHandler::localLogin))
                .POST("/auth/refresh", req -> authHandler.refresh(req.headers().header("Authorization").get(0)))
                .POST("/auth/logout", req -> authHandler.logout(req.headers().header("Authorization").get(0)))
                .POST("/auth/admin/login", req -> req.bodyToMono(LoginDTO.class).flatMap(authHandler::adminLogin))
                .POST("/auth/lawyer/login", req -> req.bodyToMono(LoginDTO.class).flatMap(authHandler::lawyerLogin))
                .build();
    }
}