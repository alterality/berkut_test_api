package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramMessage {
    @JsonProperty("message_id")
    Integer messageId;

    @JsonProperty("from")
    TelegramUser from;

    @JsonProperty("chat")
    TelegramChat chat;

    @JsonProperty("text")
    String text;

    @JsonProperty("date")
    Long date;
}
