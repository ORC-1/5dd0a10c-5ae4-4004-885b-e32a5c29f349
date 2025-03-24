package com.onlinebookstore.interswitch.boundary.cart.request;


import jakarta.annotation.Nullable;

import java.util.UUID;


public record AddToCartRequest(UUID bookId, String userId, String sessionId, int quantity) {
}
