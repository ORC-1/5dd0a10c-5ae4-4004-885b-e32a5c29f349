package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.boundary.checkout.response.CartResponse;
import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import com.onlinebookstore.interswitch.domain.cart.Cart;
import com.onlinebookstore.interswitch.domain.cart.CartItem;
import com.onlinebookstore.interswitch.domain.cart.CartRepository;
import com.onlinebookstore.interswitch.domain.checkout.Orders;
import com.onlinebookstore.interswitch.domain.checkout.OrderItem;
import com.onlinebookstore.interswitch.domain.checkout.OrderItemRepository;
import com.onlinebookstore.interswitch.domain.checkout.OrderRepository;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import com.onlinebookstore.interswitch.shared.cqrs.CommandHandler;
import com.onlinebookstore.interswitch.shared.cqrs.annotations.CommandHandle;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CommandHandle
@RequiredArgsConstructor
@Transactional
public class CheckoutCommandHandler implements CommandHandler<CheckoutCommand, CartResponse> {
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(CheckoutCommandHandler.class);

    @Override
    public CartResponse handle(CheckoutCommand cmd) {

        // Fetch cart
        Optional<Cart> optionalCart = cartRepository.findById(cmd.getCartId());
        if (optionalCart.isEmpty()) throw new IllegalArgumentException("Cart not found.");
        logger.info("cart fetched for " + (cmd.getSessionId() == "" ? cmd.getUserId() : cmd.getSessionId()));

        Cart cart = optionalCart.get();
        // Ensure cart has items
        if (cart.getCartItem().isEmpty()) throw new IllegalStateException("Cannot checkout an empty cart.");


        // Validate stock before checkout
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem item : cart.getCartItem()) {
            if (item.getBook().getStockQuantity() < item.getQuantity()) {
                logger.info("could not complete transaction as stock is either out or can not meet user order.");
                throw new IllegalStateException("Book " + item.getBook().getTitle() + " is out of stock.");
            }
        }

        // Simulate payment processing
        PaymentStatus paymentStatus = simulatePayment(cmd);

        //save all orderItems
        orderItemRepository.saveAll(orderItemList);

        // Create order
        Orders orders = new Orders();
        orders.setUserId(cart.getUserId());
        orders.setSessionId(cart.getSessionId());
        orders.setTotalAmount(cart.getTotalPrice());
        orders.setPaymentMethod(cmd.getPaymentMethod());
        orders.setStatus(paymentStatus);
        orders.setOrderItem(orderItemList);


        // Save order
        orderRepository.save(orders);

        for (CartItem item : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setBook(item.getBook());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSubtotal(item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            orderItemList.add(orderItem);
        }

        // Save all orderItems
        orderItemRepository.saveAll(orderItemList);

        // Set the orderItems to the order and save again
        orders.setOrderItem(orderItemList);
        orderRepository.save(orders);

        // Deduct stock only if payment is successful
        if (paymentStatus.equals(PaymentStatus.SUCCESS)) {
            logger.info("transaction successful.");
            for (CartItem item : cart.getCartItem()) {
                Book book = item.getBook();
                book.setStockQuantity(book.getStockQuantity() - item.getQuantity());
                bookRepository.save(book);
            }
            // Clear cart
            cartRepository.delete(cart);
            logger.info("cart cleared.");
        }

        return new CartResponse(paymentStatus.toString());
    }

    private PaymentStatus simulatePayment(CheckoutCommand cmd) {
        try {
            //make api call to  payment gateway processor
            Thread.sleep(3000); // Simulate 3 seconds of processing time
        } catch (InterruptedException e) {
            logger.error(String.format("error at CheckoutCommandHandler %s ", e));
            return PaymentStatus.FAILED;
        }
        return cmd.getMockPaymentStatusAs();
    }
}
