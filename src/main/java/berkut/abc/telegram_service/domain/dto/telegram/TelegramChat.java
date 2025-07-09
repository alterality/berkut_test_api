package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramChat {
    @JsonProperty("id")
    Long id;

    @JsonProperty("type")
    String type;

    @JsonProperty("first_name")
    String firstName;

    @JsonProperty("last_name")
    String lastName;

    @JsonProperty("username")
    String username;
}