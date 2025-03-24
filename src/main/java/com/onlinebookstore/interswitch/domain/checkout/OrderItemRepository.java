package com.onlinebookstore.interswitch.domain.checkout;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> findByOrder(Orders order);
}
