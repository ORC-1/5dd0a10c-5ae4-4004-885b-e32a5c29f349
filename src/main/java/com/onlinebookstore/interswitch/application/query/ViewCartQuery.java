package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.shared.cqrs.Query;
import java.util.Optional;
import java.util.UUID;
import lombok.Value;


@Value
public class ViewCartQuery implements Query<Optional<Cart>> {
    UUID cartId;
    UUID userId;
    String sessionId;
}
