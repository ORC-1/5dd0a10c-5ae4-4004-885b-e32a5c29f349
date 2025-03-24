package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.boundary.checkout.response.OrderResource;
import com.onlinebookstore.interswitch.shared.cqrs.Query;
import java.util.UUID;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Value
public class PurchaseHistoryQuery  implements Query<Page<OrderResource>> {
    UUID userId;
    String sessionId;
    Pageable pageable;
}
