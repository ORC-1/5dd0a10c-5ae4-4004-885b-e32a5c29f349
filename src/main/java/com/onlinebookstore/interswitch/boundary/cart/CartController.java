package com.onlinebookstore.interswitch.boundary.cart;

import com.onlinebookstore.interswitch.boundary.cart.request.AddToCartRequest;
import java.util.UUID;

import com.onlinebookstore.interswitch.boundary.cart.resource.CartResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
