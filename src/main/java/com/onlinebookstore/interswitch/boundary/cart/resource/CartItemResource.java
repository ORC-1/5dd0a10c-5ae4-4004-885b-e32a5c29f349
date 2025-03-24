package com.onlinebookstore.interswitch.boundary.cart.resource;

import com.onlinebookstore.interswitch.domain.cart.CartItem;
import java.math.BigDecimal;
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
public class CartItemResource {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private int quantity;
    private BigDecimal subtotal;

    public static CartItemResource fromEntity(CartItem cartItem) {
        return CartItemResource.builder()
                .id(cartItem.getId())
                .bookId(cartItem.getBook().getId())
                .bookTitle(cartItem.getBook().getTitle())
                .quantity(cartItem.getQuantity())
                .subtotal(cartItem.getSubtotal())
                .build();
    }
}
