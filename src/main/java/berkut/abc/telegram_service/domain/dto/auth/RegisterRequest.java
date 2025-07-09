package berkut.abc.telegram_service.domain.dto.auth;

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
public class RegisterRequest {

    @JsonProperty("login")
    @NotBlank(message = "Login is required")
    @Pattern(regexp = LOGIN_PATTERN, message = LOGIN_INVALID_MSG)
    String login;

    @JsonProperty("password")
    @NotBlank(message = "Password is required")
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_INVALID_MSG)
    String password;

    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    @Pattern(regexp = NAME_PATTERN, message = NAME_INVALID_MSG)
    String name;
}

