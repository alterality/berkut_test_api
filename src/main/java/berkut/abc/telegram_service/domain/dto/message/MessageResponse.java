package berkut.abc.telegram_service.domain.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

    @JsonProperty("id")
    Long id;

    @JsonProperty("content")
    String content;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("delivery_status")
    String deliveryStatus;

    @JsonProperty("telegram_message_id")
    Integer telegramMessageId;

    @JsonProperty("delivery_attempts")
    Integer deliveryAttempts;

    @JsonProperty("last_delivery_attempt")
    LocalDateTime lastDeliveryAttempt;

    @JsonProperty("error_message")
    String errorMessage;
}
