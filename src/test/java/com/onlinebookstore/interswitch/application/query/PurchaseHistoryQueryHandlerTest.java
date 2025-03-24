package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.boundary.checkout.response.OrderResource;
import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.checkout.OrderItem;
import com.onlinebookstore.interswitch.domain.checkout.OrderItemRepository;
import com.onlinebookstore.interswitch.domain.checkout.OrderRepository;
import com.onlinebookstore.interswitch.domain.checkout.Orders;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchaseHistoryQueryHandlerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private PurchaseHistoryQueryHandler queryHandler;

    private Pageable pageable;
    static UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void ShouldReturnMappedOrderResourcesWhenOrdersAreFound() {
        // Given
        Orders order = new Orders();
        order.setId(UUID.randomUUID());
        order.setUserId(userId);
        order.setSessionId("session456");
        order.setTotalAmount(BigDecimal.valueOf(100));
        order.setStatus(PaymentStatus.SUCCESS);
        order.setCreatedAt(LocalDateTime.now());

        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setSubtotal(BigDecimal.valueOf(50));

        Page<Orders> orderPage = new PageImpl<>(List.of(order), pageable, 1);

        doReturn(orderPage).when(orderRepository).findByUserIdOrSessionId(any(), any(), any(Pageable.class));
        doReturn(List.of(orderItem)).when(orderItemRepository).findByOrder(any(Orders.class));

        // When
        Page<OrderResource> result = queryHandler.execute(new PurchaseHistoryQuery(userId, null, pageable));

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(userId, result.getContent().get(0).getUserId());
        assertEquals(1, result.getContent().get(0).getItems().size());
        verify(orderRepository).findByUserIdOrSessionId(any(), any(), eq(pageable));
        verify(orderItemRepository).findByOrder(any(Orders.class));
    }

    @Test
    void ShouldReturnEmptyPageWhenNoOrdersAreFound() {
        // Given
        Page<Orders> emptyOrderPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        doReturn(emptyOrderPage).when(orderRepository).findByUserIdOrSessionId(any(), any(), any(Pageable.class));

        // When
        Page<OrderResource> result = queryHandler.execute(new PurchaseHistoryQuery(userId, null, pageable));

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(orderRepository).findByUserIdOrSessionId(any(), any(), eq(pageable));
        verify(orderItemRepository, never()).findByOrder(any(Orders.class));
    }

    @Test
    void ShouldThrowExceptionWhenBothUserIdAndSessionIdAreNull() {
        // Given & When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            queryHandler.execute(new PurchaseHistoryQuery(null, null, pageable));
        });

        // Then
        assertEquals("Either userId or sessionId must be provided.", exception.getMessage());
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(orderItemRepository);
    }
}
