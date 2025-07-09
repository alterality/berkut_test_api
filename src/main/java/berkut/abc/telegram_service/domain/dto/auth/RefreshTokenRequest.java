package berkut.abc.telegram_service.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RefreshTokenRequest {

    @JsonProperty("refresh_token")
    @NotBlank(message = "Refresh token is required")
    String refreshToken;
}
