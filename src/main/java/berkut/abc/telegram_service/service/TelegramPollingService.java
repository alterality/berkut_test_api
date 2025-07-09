package berkut.abc.telegram_service.service;

import berkut.abc.telegram_service.domain.dto.telegram.*;
import berkut.abc.telegram_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TelegramPollingService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${telegram.bot.token}")
    private String botToken;

    private Long lastUpdateId = 0L;

    @Scheduled(fixedDelay = 5000)
    public void pollUpdates() {
        try {
            String url = String.format(
                    "https://api.telegram.org/bot%s/getUpdates?offset=%d&timeout=20",
                    botToken,
                    lastUpdateId + 1
            );

            log.debug("Polling URL: {}", url);

            TelegramGetUpdatesResponse response = restTemplate.getForObject(
                    url,
                    TelegramGetUpdatesResponse.class
            );

            log.debug("Response: {}", response);

            if (response != null && response.getOk() != null && response.getOk()) {
                processUpdates(response.getResult());
            }
        } catch (Exception e) {
            log.error("Telegram polling error: {}", e.getMessage());
        }
    }

    @Transactional
    protected void processUpdates(List<TelegramUpdate> updates) {
        if (updates == null || updates.isEmpty()) return;

        lastUpdateId = updates.get(updates.size() - 1).getUpdateId();

        for (TelegramUpdate update : updates) {
            if (update.getMessage() != null) {
                String messageText = update.getMessage().getText();
                Long chatId = update.getMessage().getChat().getId();

                if (messageText != null && messageText.startsWith("/start ")) {
                    String token = messageText.substring(7).trim();

                    userRepository.findByTelegramToken(token).ifPresent(user -> {
                        userRepository.updateTelegramChatIdByToken(token, chatId);
                        log.info("User {} linked to Telegram chat {}", user.getLogin(), chatId);
                    });
                }
            }
        }
    }
}