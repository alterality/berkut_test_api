package berkut.abc.telegram_service.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_user_created", columnList = "user_id, created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user"})
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Message content cannot be blank")
    @Size(max = 4000, message = "Message content cannot exceed 4000 characters")
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "telegram_message_id")
    private Integer telegramMessageId;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "delivery_attempts", nullable = false)
    @Builder.Default
    private Integer deliveryAttempts = 0;

    @Column(name = "last_delivery_attempt")
    private LocalDateTime lastDeliveryAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_message_user"))
    @NotNull(message = "User cannot be null")
    private User user;

    public void markAsDelivered(Integer telegramMessageId) {
        this.telegramMessageId = telegramMessageId;
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.errorMessage = null;
        this.lastDeliveryAttempt = LocalDateTime.now();
    }

    public void markAsFailed(String errorMessage) {
        this.deliveryStatus = DeliveryStatus.FAILED;
        this.errorMessage = errorMessage;
        this.deliveryAttempts++;
        this.lastDeliveryAttempt = LocalDateTime.now();
    }

    public void markAsRetrying() {
        this.deliveryStatus = DeliveryStatus.RETRYING;
        this.deliveryAttempts++;
        this.lastDeliveryAttempt = LocalDateTime.now();
    }

    public boolean canRetry() {
        return deliveryAttempts < 3;
    }

    public enum DeliveryStatus {
        PENDING,
        DELIVERED,
        FAILED,
        RETRYING
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Message message = (Message) o;
        return getId() != null && Objects.equals(getId(), message.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}