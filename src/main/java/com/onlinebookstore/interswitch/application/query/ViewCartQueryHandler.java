package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import com.onlinebookstore.interswitch.shared.cqrs.QueryHandler;
import com.onlinebookstore.interswitch.shared.cqrs.annotations.QueryHandle;
import java.util.Optional;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@QueryHandle
public class ViewCartQueryHandler  implements QueryHandler<ViewCartQuery, Optional<Cart>> {
    private final CartRepository cartRepository;

    @Override
    public Optional<Cart> execute(ViewCartQuery query) {
        return cartRepository.findByAny(query.getCartId(), query.getUserId(), query.getSessionId());
    }
}
