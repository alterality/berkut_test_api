package berkut.abc.telegram_service.domain.mapper;

import berkut.abc.telegram_service.domain.dto.message.MessageResponse;
import berkut.abc.telegram_service.domain.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageResponse toMessageResponse(Message message);
}