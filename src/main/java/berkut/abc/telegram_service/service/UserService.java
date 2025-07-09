package berkut.abc.telegram_service.service;

import berkut.abc.telegram_service.domain.dto.user.GenerateTelegramTokenResponse;
import berkut.abc.telegram_service.domain.dto.user.UserProfileResponse;
import berkut.abc.telegram_service.domain.entity.User;
import berkut.abc.telegram_service.domain.exception.NotFoundException;
import berkut.abc.telegram_service.domain.mapper.UserMapper;
import berkut.abc.telegram_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toUserProfileResponse(user);
    }

    @Transactional
    public GenerateTelegramTokenResponse generateTelegramToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String token = UUID.randomUUID().toString();
        userRepository.updateTelegramToken(userId, token, LocalDateTime.now());

        log.info("Generated Telegram token for user: {}", user.getLogin());

        return GenerateTelegramTokenResponse.builder()
                .telegramToken(token)
                .botUsername(botUsername)
                .instructions("Send this token to the bot: /start " + token)
                .generatedAt(LocalDateTime.now())
                .build();
    }
}