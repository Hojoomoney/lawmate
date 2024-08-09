package site.lawmate.manage.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "site.lawmate.user.domain.model,site.lawmate.manage.domain.model")
public class JpaConfig {
}
