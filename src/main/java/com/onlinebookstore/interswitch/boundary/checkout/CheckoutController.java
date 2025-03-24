package com.onlinebookstore.interswitch.boundary.checkout;

import com.onlinebookstore.interswitch.boundary.checkout.request.InitializePaymentRequest;
import com.onlinebookstore.interswitch.boundary.checkout.response.OrderResource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(CheckoutController.BASE_PATH)
public interface CheckoutController {
    String BASE_PATH = "api/checkout";

    @PostMapping()
    ResponseEntity<?> checkout(@Valid @RequestBody InitializePaymentRequest initializePaymentRequest);

    @GetMapping("/purchaseHistory")
    ResponseEntity<Page<OrderResource>> getPurchaseHistory(
            @RequestParam(required = false) UUID cartId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String sessionId,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable);

}
