package com.onlinebookstore.interswitch.api.checkout;

import com.onlinebookstore.interswitch.application.command.CheckoutCommand;
import com.onlinebookstore.interswitch.application.query.PurchaseHistoryQuery;
import com.onlinebookstore.interswitch.boundary.checkout.CheckoutController;
import com.onlinebookstore.interswitch.boundary.checkout.request.InitializePaymentRequest;
import com.onlinebookstore.interswitch.boundary.checkout.response.CartResponse;
import com.onlinebookstore.interswitch.boundary.checkout.response.OrderResource;
import com.onlinebookstore.interswitch.shared.cqrs.Gate;
import com.onlinebookstore.interswitch.shared.cqrs.QueryGate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
public class CheckoutControllerImpl implements CheckoutController {
    private final Gate gate;
    private final QueryGate queryGate;

    @Override
    public ResponseEntity<CartResponse> checkout(InitializePaymentRequest req) {

        return ResponseEntity.ok(gate.execute(new CheckoutCommand(req.getCartId(),
                req.getUserId(),
                req.getSessionId(),
                req.getCurrency(),
                req.getPaymentMethod(),
                req.getMockPaymentStatusAs())));
    }

    @Override
    public ResponseEntity<Page<OrderResource>> getPurchaseHistory(UUID cartId, UUID userId, String sessionId, Pageable pageable) {
        if (userId == null && sessionId == null) {
            return ResponseEntity.badRequest().body(Page.empty());
        }
        return ResponseEntity.ok(queryGate.handle(new PurchaseHistoryQuery(userId, sessionId, pageable)));
    }
}
