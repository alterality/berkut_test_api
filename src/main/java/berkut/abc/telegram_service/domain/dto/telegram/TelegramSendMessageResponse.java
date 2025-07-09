package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramSendMessageResponse {

    @JsonProperty("ok")
    Boolean ok;

    @JsonProperty("result")
    TelegramMessageResult result;

    @JsonProperty("error_code")
    Integer errorCode;

    @JsonProperty("description")
    String description;

    @Value
    @Builder
    @Jacksonized
    public static class TelegramMessageResult {
        @JsonProperty("message_id")
        Integer messageId;

        @JsonProperty("from")
        TelegramUser from;

        @JsonProperty("chat")
        TelegramChat chat;

        @JsonProperty("date")
        Long date;

        @JsonProperty("text")
        String text;
    }
}
