package berkut.abc.telegram_service.service;

import berkut.abc.telegram_service.domain.dto.telegram.TelegramSendMessageRequest;
import berkut.abc.telegram_service.domain.dto.telegram.TelegramSendMessageResponse;
import berkut.abc.telegram_service.domain.entity.Message;
import berkut.abc.telegram_service.domain.entity.User;
import berkut.abc.telegram_service.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final RestTemplate restTemplate;
    private final MessageRepository messageRepository;

    public void sendMessageToTelegram(User user, Message message) {
        if (user.getTelegramChatId() == null) {
            log.warn("User {} has no Telegram chat ID", user.getLogin());
            return;
        }

        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        TelegramSendMessageRequest request = TelegramSendMessageRequest.builder()
                .chatId(user.getTelegramChatId())
                .text(String.format("%s, я получил от тебя сообщение:\n%s", user.getName(), message.getContent()))
                .build();

        try {
            TelegramSendMessageResponse response = restTemplate.postForObject(
                    url,
                    request,
                    TelegramSendMessageResponse.class
            );

            if (response != null && response.getOk() != null && response.getOk()) {
                if (response.getResult() != null && response.getResult().getMessageId() != null) {
                    message.markAsDelivered(response.getResult().getMessageId());
                    messageRepository.save(message);
                    log.info("Message {} sent to Telegram (ID: {})",
                            message.getId(),
                            response.getResult().getMessageId());
                }
            }
        } catch (Exception e) {
            log.error("Failed to send message to Telegram: {}", e.getMessage());
            message.markAsFailed("Telegram API error: " + e.getMessage());
            messageRepository.save(message);
        }
    }
}