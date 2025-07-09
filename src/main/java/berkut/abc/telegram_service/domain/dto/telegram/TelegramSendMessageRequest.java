package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class TelegramSendMessageRequest {

    @JsonProperty("chat_id")
    @NotNull(message = "Chat ID is required")
    Long chatId;

    @JsonProperty("text")
    @NotBlank(message = "Message text is required")
    String text;

    @JsonProperty("parse_mode")
    @Builder.Default
    String parseMode = "HTML";

    @JsonProperty("disable_web_page_preview")
    @Builder.Default
    Boolean disableWebPagePreview = true;

    @JsonProperty("disable_notification")
    @Builder.Default
    Boolean disableNotification = false;
}
