package com.onlinebookstore.interswitch.boundary.cart;

import com.onlinebookstore.interswitch.boundary.cart.request.AddToCartRequest;
import com.onlinebookstore.interswitch.boundary.cart.resource.CartResource;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@RequestMapping(CartController.BASE_PATH)
public interface CartController {
    String BASE_PATH = "api/carts";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void addToCart(@RequestBody @Valid AddToCartRequest addToCartRequest);

    @GetMapping
    ResponseEntity<CartResource> viewCart(
            @RequestParam(required = false) UUID cartId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String sessionId);

}
