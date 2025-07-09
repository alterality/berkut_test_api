package berkut.abc.telegram_service.controller;

import berkut.abc.telegram_service.config.auth.SecurityUtils;
import berkut.abc.telegram_service.domain.dto.message.MessageSendResponse;
import berkut.abc.telegram_service.domain.dto.message.MessagesPageResponse;
import berkut.abc.telegram_service.domain.dto.message.SendMessageRequest;
import berkut.abc.telegram_service.domain.dto.error.ApiResponse;
import berkut.abc.telegram_service.domain.dto.error.PageRequest;
import berkut.abc.telegram_service.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<ApiResponse<MessageSendResponse>> sendMessage(
            @Valid @RequestBody SendMessageRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Sending message for user ID: {}", userId);
        MessageSendResponse response = messageService.sendMessage(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Message sent"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<MessagesPageResponse>> getMessages(
            @Valid PageRequest pageRequest
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Fetching messages for user ID: {} with pagination: {}", userId, pageRequest);
        MessagesPageResponse response = messageService.getMessages(userId, pageRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Messages retrieved"));
    }
}