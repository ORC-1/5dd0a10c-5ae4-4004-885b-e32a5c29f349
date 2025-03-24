package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartItem;
import com.onlinebookstore.interswitch.domain.cart.CartItemRepository;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddToCartCommandHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private AddToCartCommandHandler handler;

    private UUID userId;
    private String sessionId;
    private UUID bookId;
    private Book book;
    private Cart cart;
    private AddToCartCommand command;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        sessionId = "test-session";
        bookId = UUID.randomUUID();

        book = new Book();
        book.setId(bookId);
        book.setPrice(BigDecimal.valueOf(20.00));
        book.setStockQuantity(10);

        cart = new Cart();
        cart.setUserId(userId);
        cart.setSessionId(sessionId);

        command = new AddToCartCommand(bookId, userId, sessionId, 1);
    }

    @Test
    void ShouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> handler.handle(command));

        assertEquals("Book not found.", exception.getMessage());
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(cartRepository, cartItemRepository);
    }

    @Test
    void ShouldThrowExceptionWhenBookIsOutOfStock() {
        book.setStockQuantity(0);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(IllegalStateException.class, () -> handler.handle(command));

        assertEquals("Book is out of stock.", exception.getMessage());
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(cartRepository, cartItemRepository);
    }

    @Test
    void ShouldAddNewItemToCartWhenCartExistsAndItemIsNotPresent() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.findByUserIdAndSessionId(userId, sessionId)).thenReturn(Optional.of(cart));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
        cartItem.setSubtotal(book.getPrice());

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        handler.handle(command);

        assertEquals(1, cart.getCartItem().size());
        assertEquals(BigDecimal.valueOf(20.00), cart.getTotalPrice());

        verify(cartItemRepository).save(any(CartItem.class));
        verify(cartRepository).save(cart);
    }

    @Test
    void ShouldUpdateExistingItemWhenItemAlreadyInCart() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.findByUserIdAndSessionId(userId, sessionId)).thenReturn(Optional.of(cart));

        CartItem existingItem = new CartItem();
        existingItem.setBook(book);
        existingItem.setCart(cart);
        existingItem.setQuantity(1);
        existingItem.setSubtotal(book.getPrice());

        cart.setCartItem(List.of(existingItem));

        handler.handle(command);

        assertEquals(2, existingItem.getQuantity());
        assertEquals(BigDecimal.valueOf(40.00), existingItem.getSubtotal());

        verify(cartRepository, never()).save(any(Cart.class));
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }

    @Test
    void ShouldCreateNewCartWhenCartDoesNotExist() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.findByUserIdAndSessionId(userId, sessionId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
        cartItem.setSubtotal(book.getPrice());

        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        handler.handle(command);

        assertEquals(1, cart.getCartItem().size());
        assertEquals(BigDecimal.valueOf(20.00), cart.getTotalPrice());

        // Expecting cart to be saved twice (once in findOrCreateCart, once after adding the item)
        verify(cartRepository, times(2)).save(any(Cart.class));
        verify(cartItemRepository).save(any(CartItem.class));
    }
}
