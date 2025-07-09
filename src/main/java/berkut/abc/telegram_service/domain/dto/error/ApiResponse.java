package berkut.abc.telegram_service.domain.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
@Jacksonized
public class ApiResponse<T> {

    @JsonProperty("success")
    Boolean success;

    @JsonProperty("data")
    T data;

    @JsonProperty("message")
    String message;

    @JsonProperty("timestamp")
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("errors")
    List<String> errors;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(List<String> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .errors(errors)
                .build();
    }
}

