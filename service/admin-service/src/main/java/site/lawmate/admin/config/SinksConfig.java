package site.lawmate.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;
import site.lawmate.admin.domain.model.ChatMessage;

@Configuration
public class SinksConfig {

    @Bean
    public Sinks.Many<ChatMessage> chatSink() {
        return Sinks.many().replay().all();
    }
}
