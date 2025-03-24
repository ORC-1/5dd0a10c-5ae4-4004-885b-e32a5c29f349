package com.onlinebookstore.interswitch.application.query;


import com.onlinebookstore.interswitch.boundary.checkout.response.OrderItemResource;
import com.onlinebookstore.interswitch.boundary.checkout.response.OrderResource;
import com.onlinebookstore.interswitch.domain.checkout.OrderItemRepository;
import com.onlinebookstore.interswitch.domain.checkout.OrderRepository;
import com.onlinebookstore.interswitch.domain.checkout.Orders;
import com.onlinebookstore.interswitch.shared.cqrs.QueryHandler;
import com.onlinebookstore.interswitch.shared.cqrs.annotations.QueryHandle;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@QueryHandle
@AllArgsConstructor
public class PurchaseHistoryQueryHandler implements QueryHandler<PurchaseHistoryQuery, Page<OrderResource>> {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderResource> execute(PurchaseHistoryQuery query) {
        if (query.getUserId() == null && query.getSessionId() == null) {
            throw new IllegalArgumentException("Either userId or sessionId must be provided.");
        }

        Page<Orders> orders = orderRepository.findByUserIdOrSessionId(
                query.getUserId(),
                query.getSessionId(),
                query.getPageable()
        );

        return orders.map(order -> new OrderResource(
                order.getId(),
                order.getUserId(),
                order.getSessionId(),
                order.getTotalAmount(),
                order.getStatus().toString(),
                order.getCreatedAt(),
                orderItemRepository.findByOrder(order).stream()
                        .map(item -> new OrderItemResource(
                                item.getBook().getId(),
                                item.getBook().getTitle(),
                                item.getQuantity(),
                                item.getSubtotal()
                        ))
                        .toList()
        ));

    }
}
