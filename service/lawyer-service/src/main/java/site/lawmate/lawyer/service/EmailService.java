package site.lawmate.lawyer.service;

import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<Void> sendResetPasswordEmail(String to, String newPassword);
}
