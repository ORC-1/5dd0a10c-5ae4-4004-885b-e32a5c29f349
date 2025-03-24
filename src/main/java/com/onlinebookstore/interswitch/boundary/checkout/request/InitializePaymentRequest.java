package com.onlinebookstore.interswitch.boundary.checkout.request;

import com.onlinebookstore.interswitch.shared.PaymentMethod;
import com.onlinebookstore.interswitch.shared.PaymentStatus;
import com.onlinebookstore.interswitch.shared.SupportedCurrency;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Value;



@Value
public class InitializePaymentRequest {
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

    PaymentStatus mockPaymentStatusAs = PaymentStatus.SUCCESS;
}
