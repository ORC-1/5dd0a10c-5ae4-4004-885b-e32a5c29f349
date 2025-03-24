package com.onlinebookstore.interswitch.domain.checkout;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Orders, UUID> {
    @Query("SELECT o FROM Orders o WHERE (:userId IS NOT NULL AND o.userId = :userId) "
           + "OR (:sessionId IS NOT NULL AND o.sessionId = :sessionId)")
    Page<Orders> findByUserIdOrSessionId(@Param("userId") UUID userId,
                                         @Param("sessionId") String sessionId,
                                         Pageable pageable);
}
