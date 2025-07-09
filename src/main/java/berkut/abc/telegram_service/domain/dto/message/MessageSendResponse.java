package berkut.abc.telegram_service.domain.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class MessageSendResponse {

    @JsonProperty("message_id")
    Long messageId;

    @JsonProperty("status")
    String status;

    @JsonProperty("sent_at")
    LocalDateTime sentAt;

    @JsonProperty("telegram_message_id")
    Integer telegramMessageId;
}
