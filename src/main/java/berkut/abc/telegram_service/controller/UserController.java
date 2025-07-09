package berkut.abc.telegram_service.controller;

import berkut.abc.telegram_service.config.auth.SecurityUtils;
import berkut.abc.telegram_service.domain.dto.user.GenerateTelegramTokenResponse;
import berkut.abc.telegram_service.domain.dto.user.UserProfileResponse;
import berkut.abc.telegram_service.domain.dto.error.ApiResponse;
import berkut.abc.telegram_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Fetching profile for user ID: {}", userId);
        UserProfileResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile retrieved"));
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<GenerateTelegramTokenResponse>> generateTelegramToken() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Generating Telegram token for user ID: {}", userId);
        GenerateTelegramTokenResponse response = userService.generateTelegramToken(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Telegram token generated"));
    }
}