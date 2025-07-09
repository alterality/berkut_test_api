package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramUser {
    @JsonProperty("id")
    Long id;

    @JsonProperty("is_bot")
    Boolean isBot;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("username")
    String username;
}