package berkut.abc.telegram_service.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("refresh_token")
    String refreshToken;

    @JsonProperty("token_type")
    @Builder.Default
    String tokenType = "Bearer";

    @JsonProperty("expires_in")
    Long expiresIn;

    @JsonProperty("user_info")
    UserInfo userInfo;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {
        @JsonProperty("id")
        Long id;

        @JsonProperty("login")
        String login;

        @JsonProperty("name")
        String name;

        @JsonProperty("telegram_configured")
        Boolean telegramConfigured;
    }
}
