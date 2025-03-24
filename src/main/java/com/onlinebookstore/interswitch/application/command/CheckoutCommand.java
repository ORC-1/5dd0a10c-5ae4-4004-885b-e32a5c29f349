package com.onlinebookstore.interswitch.application.command;

import com.onlinebookstore.interswitch.boundary.checkout.response.CartResponse;
import com.onlinebookstore.interswitch.shared.PaymentMethod;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import com.onlinebookstore.interswitch.shared.SupportedCurrency;
import com.onlinebookstore.interswitch.shared.cqrs.Command;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Value;


@Value
public class CheckoutCommand implements Command<CartResponse> {
    @NotNull
    UUID cartId;
    @NotNull
    String userId;
    @NotNull
    String sessionId;
    @NotNull
    SupportedCurrency currency;
    @NotNull
    PaymentMethod paymentMethod;
    PaymentStatus mockPaymentStatusAs;
}
