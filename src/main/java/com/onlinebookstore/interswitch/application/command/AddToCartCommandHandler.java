package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartItem;
import com.onlinebookstore.interswitch.domain.cart.CartItemRepository;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import com.onlinebookstore.interswitch.shared.cqrs.CommandHandler;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import com.onlinebookstore.interswitch.shared.cqrs.annotations.CommandHandle;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@CommandHandle
@RequiredArgsConstructor
@Transactional
public class AddToCartCommandHandler implements CommandHandler<AddToCartCommand, Void> {
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Void handle(AddToCartCommand cmd) {
        Optional<Book> optionalBook = bookRepository.findById(cmd.getBook());
        if (optionalBook.isEmpty())
            throw new IllegalArgumentException("Book not found.");
        Book book = optionalBook.get();
        // Check if the book is in stock
        if (book.getStockQuantity() < 1) {
            throw new IllegalStateException("Book is out of stock.");
        }

        // Find existing cart or create a new one
        Cart cart = findOrCreateCart(cmd.getUserId(), cmd.getSessionId());
        // Check if item is already in the cart
        Optional<CartItem> existingItem = cart.getCartItem().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst();


        if (existingItem.isPresent()) {
            // Update quantity & subtotal
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartItem.setSubtotal(cartItem.getBook().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setBook(book);
            cartItem.setQuantity(cmd.getQuantity());
            cartItem.setSubtotal(book.getPrice());

            // Save new cart item
            cart.getCartItem().add(cartItem);
            cartItemRepository.save(cartItem);

            // Update total price of the cart
            cart.getCartItem().forEach(item ->
                    item.setSubtotal(item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))));
            BigDecimal newTotal = cart.getCartItem().stream()
                    .map(CartItem::getSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            cart.setTotalPrice(newTotal);

            // Save the cart
            cartRepository.save(cart);
        }
        return null;
    }

    private Cart findOrCreateCart(UUID userId, String sessionId) {
        return cartRepository.findByUserIdAndSessionId(userId, sessionId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    newCart.setSessionId(sessionId);
                    return cartRepository.save(newCart);
                });
    }
}
