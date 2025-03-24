package com.onlinebookstore.interswitch.domain.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByUserIdAndSessionId(UUID userId, String sessionId);

    @Query("SELECT c FROM Cart c WHERE c.id = :cartId OR c.userId = :userId OR c.sessionId = :sessionId")
    Optional<Cart> findByAny(@Param("cartId") UUID cartId, @Param("userId") UUID userId, @Param("sessionId") String sessionId);
}
