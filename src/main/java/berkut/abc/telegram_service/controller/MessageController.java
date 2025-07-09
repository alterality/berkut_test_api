package berkut.abc.telegram_service.controller;

import berkut.abc.telegram_service.config.auth.SecurityUtils;
import berkut.abc.telegram_service.domain.dto.message.MessageSendResponse;
import berkut.abc.telegram_service.domain.dto.message.MessagesPageResponse;
import berkut.abc.telegram_service.domain.dto.message.SendMessageRequest;
import berkut.abc.telegram_service.domain.dto.error.ApiResponseDto;
import berkut.abc.telegram_service.domain.dto.error.PageRequest;
import berkut.abc.telegram_service.service.MessageService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Messages", description = "Endpoints for message operations")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Send a message",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Message sent",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @PostMapping
    public ResponseEntity<ApiResponseDto<MessageSendResponse>> sendMessage(
            @Valid @RequestBody SendMessageRequest request
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Sending message for user ID: {}", userId);
        MessageSendResponse response = messageService.sendMessage(userId, request);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Message sent"));
    }

    @Operation(summary = "Get user's messages",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Messages retrieved",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @GetMapping
    public ResponseEntity<ApiResponseDto<MessagesPageResponse>> getMessages(
            @Valid PageRequest pageRequest
    ) {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Fetching messages for user ID: {} with pagination: {}", userId, pageRequest);
        MessagesPageResponse response = messageService.getMessages(userId, pageRequest);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Messages retrieved"));
    }
}