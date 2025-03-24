package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewCartQueryHandlerTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ViewCartQueryHandler queryHandler;

    private ViewCartQuery query;

    static UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        query = new ViewCartQuery(UUID.randomUUID(), userId, "session456");
    }

    @Test
    void execute_ShouldReturnCart_WhenCartExists() {
        UUID cartId = UUID.randomUUID();
        // Given
        Cart cart = new Cart();
        cart.setId(cartId);

        doReturn(Optional.of(cart)).when(cartRepository).findByAny(any(), any(), any());

        // When
        Optional<Cart> result = queryHandler.execute(query);

        // Then
        assertTrue(result.isPresent());
        assertEquals(cartId, result.get().getId());
        verify(cartRepository).findByAny(any(), any(), any());
    }

    @Test
    void execute_ShouldReturnEmpty_WhenCartDoesNotExist() {
        // Given
        doReturn(Optional.empty()).when(cartRepository).findByAny(any(), any(), any());

        // When
        Optional<Cart> result = queryHandler.execute(query);

        // Then
        assertTrue(result.isEmpty());
        verify(cartRepository).findByAny(any(), any(), any());
    }
}
