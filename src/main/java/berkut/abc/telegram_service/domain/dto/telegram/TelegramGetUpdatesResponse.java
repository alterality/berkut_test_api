package berkut.abc.telegram_service.domain.dto.telegram;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class TelegramGetUpdatesResponse {
    @JsonProperty("ok")
    Boolean ok;

    @JsonProperty("result")
    List<TelegramUpdate> result;
}

