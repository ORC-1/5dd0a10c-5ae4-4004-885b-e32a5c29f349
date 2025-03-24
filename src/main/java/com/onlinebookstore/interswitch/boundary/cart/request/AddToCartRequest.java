package com.onlinebookstore.interswitch.boundary.cart.request;

import java.util.UUID;


public record AddToCartRequest(UUID bookId, String userId, String sessionId, int quantity) {
}
