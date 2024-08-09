package site.lawmate.lawyer.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.lawmate.lawyer.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    @Override
    public Mono<Void> sendResetPasswordEmail(String to, String newPassword) {
        return Mono.fromRunnable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);

                helper.setTo(to);
                helper.setSubject("비밀번호 재설정 안내");
                helper.setText("새로운 비밀번호는 다음과 같습니다: " + newPassword);

                mailSender.send(message);
            } catch (MessagingException e) {
                throw new RuntimeException("이메일 전송 실패", e);
            }
        });
    }
}
