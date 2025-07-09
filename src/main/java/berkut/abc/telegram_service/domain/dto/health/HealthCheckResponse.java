package berkut.abc.telegram_service.domain.dto.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder
@Jacksonized
public class HealthCheckResponse {

    @JsonProperty("status")
    String status;

    @JsonProperty("timestamp")
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();

    @JsonProperty("services")
    Map<String, ServiceHealth> services;

    @JsonProperty("version")
    String version;

    @JsonProperty("uptime")
    Long uptime;

    @Value
    @Builder
    @Jacksonized
    public static class ServiceHealth {
        @JsonProperty("status")
        String status;

        @JsonProperty("message")
        String message;

        @JsonProperty("response_time")
        Long responseTime;
    }
}