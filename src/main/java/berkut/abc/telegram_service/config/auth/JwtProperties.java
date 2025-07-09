package berkut.abc.telegram_service.config.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;
    private long accessTokenExpiration = 3600000;
    private long refreshTokenExpiration = 86400000;
    private String tokenPrefix = "Bearer ";
    private String headerName = "Authorization";
    private String issuer = "telegram-bot-api";
}