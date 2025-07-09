package berkut.abc.telegram_service.service;

import berkut.abc.telegram_service.domain.dto.message.MessageSendResponse;
import berkut.abc.telegram_service.domain.dto.message.MessagesPageResponse;
import berkut.abc.telegram_service.domain.dto.message.SendMessageRequest;
import berkut.abc.telegram_service.domain.dto.error.PageRequest;
import berkut.abc.telegram_service.domain.entity.Message;
import berkut.abc.telegram_service.domain.entity.User;
import berkut.abc.telegram_service.domain.exception.NotFoundException;
import berkut.abc.telegram_service.domain.mapper.MessageMapper;
import berkut.abc.telegram_service.domain.repository.MessageRepository;
import berkut.abc.telegram_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final TelegramService telegramService;

    @Transactional
    public MessageSendResponse sendMessage(Long userId, SendMessageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Message message = new Message();
        message.setContent(request.getContent());
        message.setUser(user);
        message.setCreatedAt(LocalDateTime.now());

        message = messageRepository.save(message);
        log.info("Message saved: ID {}", message.getId());

        if (user.isTelegramConfigured()) {
            telegramService.sendMessageToTelegram(user, message);
        }

        return MessageSendResponse.builder()
                .messageId(message.getId())
                .status("SENT")
                .sentAt(LocalDateTime.now())
                .telegramMessageId(message.getTelegramMessageId())
                .build();
    }


    @Transactional(readOnly = true)
    public MessagesPageResponse getMessages(Long userId, PageRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Sort.Direction direction = "DESC".equalsIgnoreCase(request.getDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                request.getPage(),
                request.getSize(),
                sort
        );

        Page<Message> page = messageRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        return MessagesPageResponse.builder()
                .messages(page.getContent().stream()
                        .map(messageMapper::toMessageResponse)
                        .collect(Collectors.toList()))
                .pagination(MessagesPageResponse.PaginationInfo.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .first(page.isFirst())
                        .last(page.isLast())
                        .build())
                .build();
    }
}