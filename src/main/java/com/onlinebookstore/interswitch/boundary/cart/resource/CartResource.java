package com.onlinebookstore.interswitch.boundary.cart.resource;

import com.onlinebookstore.interswitch.domain.cart.Cart;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResource {
    private UUID id;
    private List<CartItemResource> items;
    private BigDecimal totalPrice;
    private boolean isGuestCart; // True if session-based, False if user-based

    public static CartResource fromEntity(Cart cart) {
        return CartResource.builder()
                .id(cart.getId())
                .items(cart.getCartItem().stream().map(CartItemResource::fromEntity).toList())
                .totalPrice(cart.getTotalPrice())
                .isGuestCart(cart.getUserId() == null) // If no user, it's a guest cart
                .build();
    }
}
