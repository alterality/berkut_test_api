package berkut.abc.telegram_service.domain.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class ErrorResponse {

    @JsonProperty("error")
    String error;

    @JsonProperty("message")
    String message;

    @JsonProperty("status")
    Integer status;

    @JsonProperty("timestamp")
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("path")
    String path;

    @JsonProperty("validation_errors")
    Map<String, String> validationErrors;
}
