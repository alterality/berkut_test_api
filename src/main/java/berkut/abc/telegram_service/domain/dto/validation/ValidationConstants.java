package berkut.abc.telegram_service.domain.dto.validation;

public final class ValidationConstants {

    public static final String LOGIN_PATTERN = "^[a-zA-Z0-9._-]{3,50}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String NAME_PATTERN = "^[\\p{L}\\p{M}\\s.-]{2,100}$";
    public static final String MESSAGE_CONTENT_PATTERN = "^[\\s\\S]{1,4000}$";
    public static final String TELEGRAM_TOKEN_PATTERN = "^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
    public static final String LOGIN_INVALID_MSG = "Login must contain only letters, numbers, dots, underscores, and hyphens (3-50 characters)";
    public static final String PASSWORD_INVALID_MSG = "Password must contain at least 8 characters with uppercase, lowercase, digit, and special character";
    public static final String NAME_INVALID_MSG = "Name must contain only letters, spaces, dots, and hyphens (2-100 characters)";
    public static final String MESSAGE_INVALID_MSG = "Message content must be between 1 and 4000 characters";

    private ValidationConstants() {
        throw new UnsupportedOperationException("Utility class");
    }
}
