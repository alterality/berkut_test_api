package berkut.abc.telegram_service.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class GenerateTelegramTokenResponse {

    @JsonProperty("telegram_token")
    String telegramToken;

    @JsonProperty("bot_username")
    String botUsername;

    @JsonProperty("instructions")
    String instructions;

    @JsonProperty("generated_at")
    LocalDateTime generatedAt;
}
