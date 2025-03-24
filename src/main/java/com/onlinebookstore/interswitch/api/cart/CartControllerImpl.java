package com.onlinebookstore.interswitch.api.cart;

import com.onlinebookstore.interswitch.application.command.AddToCartCommand;
import com.onlinebookstore.interswitch.application.query.ViewCartQuery;
import com.onlinebookstore.interswitch.boundary.cart.CartController;
import com.onlinebookstore.interswitch.boundary.cart.request.AddToCartRequest;
import com.onlinebookstore.interswitch.boundary.cart.resource.CartResource;
import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.shared.cqrs.Gate;
import com.onlinebookstore.interswitch.shared.cqrs.QueryGate;
import com.onlinebookstore.interswitch.shared.exception.BadRequestException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class CartControllerImpl implements CartController {
    private final Gate gate;
    private final QueryGate queryGate;

    @Override
    public void addToCart(AddToCartRequest req) {
        Objects.requireNonNull(req.bookId(), "Book ID cannot be null.");
        if (req.userId() == null && req.sessionId() == null)
            throw new BadRequestException("both UserId and SessionId can not be null.");
        UUID userId = null;
        //Handle Invalid UUID string
        if (req.userId() != null && !req.userId().isEmpty())  userId = UUID.fromString(req.userId());
        gate.execute(new AddToCartCommand(req.bookId(), userId, req.sessionId(), req.quantity()));
    }

    @Override
    public ResponseEntity<CartResource> viewCart(UUID cartId, UUID userId, String sessionId) {
        if (cartId == null && userId == null && sessionId == null)
            throw new BadRequestException("CartId, UserId and SessionId can not all be null.");
        Optional<Cart> cart = queryGate.handle(new ViewCartQuery(cartId, userId, sessionId));

        return cart.map(value -> ResponseEntity.ok(CartResource.fromEntity(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
