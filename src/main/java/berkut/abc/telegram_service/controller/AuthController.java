package berkut.abc.telegram_service.controller;

import berkut.abc.telegram_service.domain.dto.auth.AuthResponse;
import berkut.abc.telegram_service.domain.dto.auth.LoginRequest;
import berkut.abc.telegram_service.domain.dto.auth.RefreshTokenRequest;
import berkut.abc.telegram_service.domain.dto.auth.RegisterRequest;
import berkut.abc.telegram_service.domain.dto.error.ApiResponseDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import berkut.abc.telegram_service.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user",

            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Register request for login: {}", request.getLogin());
        AuthResponse response = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDto.success(response, "User registered successfully"));
    }

    @Operation(summary = "Authenticate user and get tokens",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Login request for user: {}", request.getLogin());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Login successful"));
    }

    @Operation(summary = "Refresh JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid token",
                            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))
            })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDto<AuthResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        log.info("Refresh token request");
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponseDto.success(response, "Token refreshed successfully"));
    }

    @Operation(summary = "Logout user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logout successful")
            })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto<Void>> logout() {
        log.info("Logout request for user");
        authService.logout();
        return ResponseEntity.ok(ApiResponseDto.success(null, "Logout successful"));
    }
}