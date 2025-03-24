package com.onlinebookstore.interswitch.boundary.checkout.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Value;

@Value
public class OrderResource {
    UUID id;
    UUID userId;
    String sessionId;
    BigDecimal totalAmount;
    String status;
    LocalDateTime createdAt;
    List<OrderItemResource> items;
}
