package berkut.abc.telegram_service.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import static berkut.abc.telegram_service.domain.dto.validation.ValidationConstants.*;

@Value
@Builder
@Jacksonized
public class UpdateUserRequest {

    @JsonProperty("name")
    @Pattern(regexp = NAME_PATTERN, message = NAME_INVALID_MSG)
    String name;

    @JsonProperty("password")
    @Pattern(regexp = PASSWORD_PATTERN, message = PASSWORD_INVALID_MSG)
    String password;
}
