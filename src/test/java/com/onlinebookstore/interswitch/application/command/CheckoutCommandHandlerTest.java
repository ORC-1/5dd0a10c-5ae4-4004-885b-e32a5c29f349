package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.boundary.checkout.response.CartResponse;
import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartItem;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import com.onlinebookstore.interswitch.domain.checkout.Orders;
import com.onlinebookstore.interswitch.domain.checkout.OrderItemRepository;
import com.onlinebookstore.interswitch.domain.checkout.OrderRepository;
import com.onlinebookstore.interswitch.shared.PaymentMethod;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import com.onlinebookstore.interswitch.shared.SupportedCurrency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutCommandHandlerTest {

    @Mock private CartRepository cartRepository;
    @Mock private BookRepository bookRepository;
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks private CheckoutCommandHandler handler;

    private CheckoutCommand command;
    private Cart cart;
    private Book book;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        String sessionId = "test-session";

        book = new Book();
        book.setId(UUID.randomUUID());
        book.setTitle("Test Book");
        book.setStockQuantity(10);
        book.setPrice(BigDecimal.valueOf(50.00));

        cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);
        cartItem.setSubtotal(book.getPrice().multiply(BigDecimal.valueOf(2)));

        cart = new Cart();
        cart.setId(cartId);
        cart.setUserId(userId);
        cart.setSessionId(sessionId);
        cart.setCartItem(Collections.singletonList(cartItem));
        cart.setTotalPrice(cartItem.getSubtotal());

        command = new CheckoutCommand(cartId, userId.toString(), sessionId, SupportedCurrency.NGN, PaymentMethod.WEB, PaymentStatus.SUCCESS);
    }

    @Test
    void ShouldCompleteCheckoutWhenPaymentIsSuccessful() {
        when(cartRepository.findById(command.getCartId())).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartResponse response = handler.handle(command);

        assertEquals(response.getMessage(), PaymentStatus.SUCCESS.toString());
        verify(orderRepository, times(2)).save(any(Orders.class));
        verify(orderItemRepository, times(2)).saveAll(anyList());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    void ShouldFailWhenCartIsNotFound() {
        when(cartRepository.findById(command.getCartId())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> handler.handle(command));
    }

    @Test
    void ShouldFailWhenCartIsEmpty() {
        cart.setCartItem(Collections.emptyList());
        when(cartRepository.findById(command.getCartId())).thenReturn(Optional.of(cart));
        assertThrows(IllegalStateException.class, () -> handler.handle(command));
    }

    @Test
    void ShouldFailWhenStockIsInsufficient() {
        book.setStockQuantity(1);
        when(cartRepository.findById(command.getCartId())).thenReturn(Optional.of(cart));
        assertThrows(IllegalStateException.class, () -> handler.handle(command));
    }
}
