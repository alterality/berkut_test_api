package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramUpdate {
    @JsonProperty("update_id")
    Long updateId;

    @JsonProperty("message")
    TelegramMessage message;
}
