package berkut.abc.telegram_service.domain.dto.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class PageRequest {

    @JsonProperty("page")
    @Builder.Default
    Integer page = 0;

    @JsonProperty("size")
    @Builder.Default
    Integer size = 20;

    @JsonProperty("sort")
    String sort;

    @JsonProperty("direction")
    @Builder.Default
    String direction = "DESC";
}
