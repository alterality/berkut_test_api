package berkut.abc.telegram_service.service;

import berkut.abc.telegram_service.config.auth.SecurityUtils;
import berkut.abc.telegram_service.config.auth.UserPrincipal;
import berkut.abc.telegram_service.domain.dto.auth.AuthResponse;
import berkut.abc.telegram_service.domain.dto.auth.LoginRequest;
import berkut.abc.telegram_service.domain.dto.auth.RefreshTokenRequest;
import berkut.abc.telegram_service.domain.dto.auth.RegisterRequest;
import berkut.abc.telegram_service.domain.entity.User;
import berkut.abc.telegram_service.domain.exception.AuthException;
import berkut.abc.telegram_service.domain.mapper.UserMapper;
import berkut.abc.telegram_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByLoginIgnoreCase(request.getLogin())) {
            throw new AuthException("Login already exists");
        }

        User user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .isActive(true)
                .build();

        userRepository.save(user);
        log.info("User registered: {}", user.getLogin());

        return generateAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByLoginIgnoreCase(request.getLogin())
                .orElseThrow(() -> new AuthException("User not found"));

        if (!user.getIsActive()) {
            throw new AuthException("User account is disabled");
        }

        log.info("User authenticated: {}", user.getLogin());
        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (jwtService.isTokenBlacklisted(refreshToken)) {
            throw new AuthException("Token is invalidated");
        }

        String username = jwtService.extractUsername(refreshToken);
        if (!"refresh".equals(jwtService.extractTokenType(refreshToken))) {
            throw new AuthException("Invalid token type");
        }

        User user = userRepository.findByLoginIgnoreCase(username)
                .orElseThrow(() -> new AuthException("User not found"));

        jwtService.blacklistToken(refreshToken);
        return generateAuthResponse(user);
    }

    public void logout() {
        String token = SecurityUtils.getCurrentJwt();
        if (token != null) {
            jwtService.blacklistToken(token);
            log.info("User logged out: {}", SecurityUtils.getCurrentUsername());
        }
    }

    private AuthResponse generateAuthResponse(User user) {
        UserPrincipal principal = UserPrincipal.create(user);
        String accessToken = jwtService.generateAccessToken(principal);
        String refreshToken = jwtService.generateRefreshToken(principal);
        long expiresIn = jwtService.getExpirationTime("access");

        return userMapper.toAuthResponse(
                user,
                accessToken,
                refreshToken,
                expiresIn
        );
    }
}