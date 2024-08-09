package site.lawmate.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

@Configuration //자바 설정 클래스(=>빈 객체)가 된다
public class ServerConfig {
    @Bean
    public String datePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss.XXX";
    }

    @Bean
    public DateFormatter defaultDateFormatter() {
        return new DateFormatter(datePattern());
    }
}
