package berkut.abc.telegram_service.domain.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class MessagesPageResponse {

    @JsonProperty("messages")
    List<MessageResponse> messages;

    @JsonProperty("pagination")
    PaginationInfo pagination;

    @Value
    @Builder
    @Jacksonized
    public static class PaginationInfo {
        @JsonProperty("page")
        Integer page;

        @JsonProperty("size")
        Integer size;

        @JsonProperty("total_elements")
        Long totalElements;

        @JsonProperty("total_pages")
        Integer totalPages;

        @JsonProperty("first")
        Boolean first;

        @JsonProperty("last")
        Boolean last;
    }
}
