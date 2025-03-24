package com.onlinebookstore.interswitch.domain.checkout;

import com.onlinebookstore.interswitch.shared.PaymentMethod;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Orders {
    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;
    private String sessionId;
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItem;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private LocalDateTime createdAt = LocalDateTime.now();
}
