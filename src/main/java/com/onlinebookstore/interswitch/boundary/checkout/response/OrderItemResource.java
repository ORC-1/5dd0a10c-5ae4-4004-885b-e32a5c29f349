package com.onlinebookstore.interswitch.boundary.checkout.response;

import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class OrderItemResource {
    UUID bookId;
    String title;
    int quantity;
    BigDecimal price;
}
