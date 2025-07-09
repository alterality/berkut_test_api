package berkut.abc.telegram_service.controller;

import berkut.abc.telegram_service.config.auth.SecurityUtils;
import berkut.abc.telegram_service.domain.dto.user.GenerateTelegramTokenResponse;
import berkut.abc.telegram_service.domain.dto.user.UserProfileResponse;
import berkut.abc.telegram_service.domain.dto.error.ApiResponseDto;
import berkut.abc.telegram_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Tag(name = "Users", description = "Endpoints for user profile and Telegram integration")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile retrieved",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserProfileResponse>> getProfile() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Fetching profile for user ID: {}", userId);
        UserProfileResponse response = userService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Profile retrieved"));
    }

    @Operation(summary = "Generate Telegram token for chat binding",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Telegram token generated",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @PostMapping("/token")
    public ResponseEntity<ApiResponseDto<GenerateTelegramTokenResponse>> generateTelegramToken() {
        Long userId = SecurityUtils.getCurrentUserId();
        log.info("Generating Telegram token for user ID: {}", userId);
        GenerateTelegramTokenResponse response = userService.generateTelegramToken(userId);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Telegram token generated"));
    }
}