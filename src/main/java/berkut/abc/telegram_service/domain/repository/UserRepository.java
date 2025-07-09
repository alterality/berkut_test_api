package berkut.abc.telegram_service.domain.repository;

import berkut.abc.telegram_service.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find user by login (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.login) = LOWER(:login)")
    Optional<User> findByLoginIgnoreCase(@Param("login") String login);

    /**
     * Find user by telegram token
     */
    Optional<User> findByTelegramToken(String telegramToken);

    @Modifying
    @Query("UPDATE User u SET u.telegramChatId = :chatId WHERE u.telegramToken = :token")
    void updateTelegramChatIdByToken(@Param("token") String token, @Param("chatId") Long chatId);

    /**
     * Find user by telegram chat ID
     */
    Optional<User> findByTelegramChatId(Long telegramChatId);

    /**
     * Check if login exists (case-insensitive)
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE LOWER(u.login) = LOWER(:login)")
    boolean existsByLoginIgnoreCase(@Param("login") String login);

    /**
     * Check if telegram token exists
     */
    boolean existsByTelegramToken(String telegramToken);

    /**
     * Find all active users
     */
    List<User> findByIsActiveTrue();

    /**
     * Find users with configured telegram (have both token and chat ID)
     */
    @Query("SELECT u FROM User u WHERE u.telegramToken IS NOT NULL AND u.telegramChatId IS NOT NULL AND u.isActive = true")
    List<User> findUsersWithConfiguredTelegram();

    /**
     * Find users created after specified date
     */
    List<User> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find users by role - REMOVED (no role system)
     */
    // List<User> findByRole(User.Role role);

    /**
     * Find users with pagination and activity status
     */
    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    /**
     * Search users by name or login (case-insensitive)
     */
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.login) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchByNameOrLogin(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Update user's telegram chat ID by telegram token
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.telegramChatId = :chatId, u.updatedAt = :updateTime WHERE u.telegramToken = :token")
    int updateTelegramChatIdByToken(@Param("token") String token,
                                    @Param("chatId") Long chatId,
                                    @Param("updateTime") LocalDateTime updateTime);

    /**
     * Deactivate user by ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.isActive = false, u.updatedAt = :updateTime WHERE u.id = :userId")
    int deactivateUser(@Param("userId") Long userId, @Param("updateTime") LocalDateTime updateTime);

    /**
     * Generate and update telegram token for user
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.telegramToken = :token, u.updatedAt = :updateTime WHERE u.id = :userId")
    int updateTelegramToken(@Param("userId") Long userId,
                            @Param("token") String token,
                            @Param("updateTime") LocalDateTime updateTime);

    /**
     * Clear telegram configuration (token and chat ID)
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.telegramToken = null, u.telegramChatId = null, u.updatedAt = :updateTime WHERE u.id = :userId")
    int clearTelegramConfiguration(@Param("userId") Long userId, @Param("updateTime") LocalDateTime updateTime);

    /**
     * Count active users
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isActive = true")
    long countActiveUsers();

    /**
     * Count users with configured telegram
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.telegramToken IS NOT NULL AND u.telegramChatId IS NOT NULL AND u.isActive = true")
    long countUsersWithConfiguredTelegram();

    /**
     * Find users registered in date range
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate ORDER BY u.createdAt DESC")
    List<User> findUsersRegisteredBetween(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Find users with messages count
     */
    @Query("SELECT u, COUNT(m) as messageCount FROM User u LEFT JOIN u.messages m " +
            "WHERE u.isActive = true GROUP BY u ORDER BY messageCount DESC")
    Page<Object[]> findUsersWithMessageCount(Pageable pageable);
}