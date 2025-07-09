package berkut.abc.telegram_service.domain.repository;

import berkut.abc.telegram_service.domain.entity.User;
import berkut.abc.telegram_service.domain.entity.Message;
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
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    /**
     * Find all messages for a specific user ordered by creation date (newest first)
     */
    Page<Message> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * Find all messages for a specific user ID ordered by creation date (newest first)
     */
    Page<Message> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find messages by user and delivery status
     */
    List<Message> findByUserAndDeliveryStatus(User user, Message.DeliveryStatus deliveryStatus);

    /**
     * Find messages by delivery status
     */
    List<Message> findByDeliveryStatus(Message.DeliveryStatus deliveryStatus);

    /**
     * Find messages that failed and can be retried
     */
    @Query("SELECT m FROM Message m WHERE m.deliveryStatus = 'FAILED' AND m.deliveryAttempts < 3")
    List<Message> findMessagesForRetry();

    /**
     * Find messages created in date range for specific user
     */
    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.createdAt BETWEEN :startDate AND :endDate ORDER BY m.createdAt DESC")
    Page<Message> findByUserIdAndCreatedAtBetween(@Param("userId") Long userId,
                                                  @Param("startDate") LocalDateTime startDate,
                                                  @Param("endDate") LocalDateTime endDate,
                                                  Pageable pageable);

    /**
     * Find messages by user and content contains (case-insensitive)
     */
    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND LOWER(m.content) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY m.createdAt DESC")
    Page<Message> findByUserIdAndContentContainsIgnoreCase(@Param("userId") Long userId,
                                                           @Param("searchTerm") String searchTerm,
                                                           Pageable pageable);

    /**
     * Count messages by user
     */
    long countByUser(User user);

    /**
     * Count messages by user ID
     */
    long countByUserId(Long userId);

    /**
     * Count messages by delivery status
     */
    long countByDeliveryStatus(Message.DeliveryStatus deliveryStatus);

    /**
     * Count messages for user by delivery status
     */
    long countByUserAndDeliveryStatus(User user, Message.DeliveryStatus deliveryStatus);

    /**
     * Find latest message for user
     */
    Optional<Message> findTopByUserOrderByCreatedAtDesc(User user);

    /**
     * Find latest message for user ID
     */
    Optional<Message> findTopByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Find messages created after specific date
     */
    List<Message> findByCreatedAtAfter(LocalDateTime date);

    /**
     * Find messages created today for user
     */
    @Query("SELECT m FROM Message m WHERE m.user.id = :userId AND m.createdAt >= :startDate AND m.createdAt < :endDate ORDER BY m.createdAt DESC")
    List<Message> findTodaysMessagesByUserId(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Update message delivery status
     */
    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.deliveryStatus = :status, m.lastDeliveryAttempt = :attemptTime WHERE m.id = :messageId")
    int updateDeliveryStatus(@Param("messageId") Long messageId,
                             @Param("status") Message.DeliveryStatus status,
                             @Param("attemptTime") LocalDateTime attemptTime);

    /**
     * Mark message as delivered
     */
    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.deliveryStatus = 'DELIVERED', m.telegramMessageId = :telegramMessageId, " +
            "m.lastDeliveryAttempt = :deliveryTime, m.errorMessage = null WHERE m.id = :messageId")
    int markAsDelivered(@Param("messageId") Long messageId,
                        @Param("telegramMessageId") Integer telegramMessageId,
                        @Param("deliveryTime") LocalDateTime deliveryTime);

    /**
     * Mark message as failed
     */
    @Modifying
    @Transactional
    @Query("UPDATE Message m SET m.deliveryStatus = 'FAILED', m.errorMessage = :errorMessage, " +
            "m.deliveryAttempts = m.deliveryAttempts + 1, m.lastDeliveryAttempt = :attemptTime WHERE m.id = :messageId")
    int markAsFailed(@Param("messageId") Long messageId,
                     @Param("errorMessage") String errorMessage,
                     @Param("attemptTime") LocalDateTime attemptTime);

    /**
     * Get message statistics for user
     */
    @Query("SELECT m.deliveryStatus, COUNT(m) FROM Message m WHERE m.user.id = :userId GROUP BY m.deliveryStatus")
    List<Object[]> getMessageStatisticsByUserId(@Param("userId") Long userId);

    /**
     * Get overall message statistics
     */
    @Query("SELECT m.deliveryStatus, COUNT(m) FROM Message m GROUP BY m.deliveryStatus")
    List<Object[]> getOverallMessageStatistics();

    /**
     * Find messages with delivery attempts greater than specified count
     */
    @Query("SELECT m FROM Message m WHERE m.deliveryAttempts > :attempts ORDER BY m.createdAt DESC")
    List<Message> findMessagesWithManyAttempts(@Param("attempts") Integer attempts);

    /**
     * Delete messages older than specified date
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Message m WHERE m.createdAt < :date")
    int deleteMessagesOlderThan(@Param("date") LocalDateTime date);

    /**
     * Find messages by multiple delivery statuses
     */
    @Query("SELECT m FROM Message m WHERE m.deliveryStatus IN :statuses ORDER BY m.createdAt DESC")
    List<Message> findByDeliveryStatusIn(@Param("statuses") List<Message.DeliveryStatus> statuses);

    /**
     * Get daily message count for user in date range
     */
    @Query("SELECT DATE(m.createdAt) as date, COUNT(m) as count FROM Message m " +
            "WHERE m.user.id = :userId AND m.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(m.createdAt) ORDER BY date DESC")
    List<Object[]> getDailyMessageCountByUserId(@Param("userId") Long userId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}