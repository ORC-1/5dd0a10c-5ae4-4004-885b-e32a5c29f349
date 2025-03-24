package com.onlinebookstore.interswitch.domain.cart;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
}
