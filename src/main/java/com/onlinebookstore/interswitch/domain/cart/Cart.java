package com.onlinebookstore.interswitch.domain.cart;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cart {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @Size(min = 1, message = "Cart must contain at least one item.")
    private List<CartItem> cartItem = new ArrayList<>();

    @NotNull(message = "Total price must not be null.")
    @PositiveOrZero(message = "Total price cannot be negative.")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Nullable
    private UUID userId; // Null for guest users
    @Nullable
    private String sessionId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Cart(UUID id, List<CartItem> items, BigDecimal totalPrice, @Nullable UUID userId, @Nullable String sessionId) {
        if (userId == null && (sessionId == null || sessionId.isEmpty()))
            throw new IllegalArgumentException("Either userId or sessionId must be provided.");

        this.id = id;
        this.cartItem = items != null ? items : new ArrayList<>();
        this.totalPrice = totalPrice != null ? totalPrice : BigDecimal.ZERO;
        this.userId = userId;
        this.sessionId = sessionId;
    }

    @AssertTrue(message = "Either userId or sessionId must be provided.")
    public boolean isValidCartOwner() {
        return userId != null || (sessionId != null && !sessionId.isEmpty());
    }

}
