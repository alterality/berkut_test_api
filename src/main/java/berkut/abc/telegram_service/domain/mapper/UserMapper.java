package berkut.abc.telegram_service.domain.mapper;

import berkut.abc.telegram_service.domain.dto.auth.AuthResponse;
import berkut.abc.telegram_service.domain.dto.user.UserProfileResponse;
import berkut.abc.telegram_service.domain.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserProfileResponse toUserProfileResponse(User user);

    default AuthResponse toAuthResponse(User user, String accessToken, String refreshToken, Long expiresIn) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .userInfo(toUserInfo(user))
                .build();
    }
    default AuthResponse.UserInfo toUserInfo(User user) {
        return AuthResponse.UserInfo.builder()
                .id(user.getId())
                .login(user.getLogin())
                .name(user.getName())
                .telegramConfigured(user.isTelegramConfigured())
                .build();
    }
}

