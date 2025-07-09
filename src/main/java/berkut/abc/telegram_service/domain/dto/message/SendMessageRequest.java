package berkut.abc.telegram_service.domain.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static berkut.abc.telegram_service.domain.dto.validation.ValidationConstants.*;

@Value
@Builder
@Jacksonized
public class SendMessageRequest {

    @JsonProperty("content")
    @NotBlank(message = "Message content is required")
    @Pattern(regexp = MESSAGE_CONTENT_PATTERN, message = MESSAGE_INVALID_MSG)
    String content;
}

