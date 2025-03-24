package com.onlinebookstore.interswitch.boundary.checkout.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Value;


@Value
public class OrderItemResource {
    UUID bookId;
    String title;
    int quantity;
    BigDecimal price;
}
