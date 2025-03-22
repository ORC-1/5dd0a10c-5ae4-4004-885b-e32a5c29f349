package com.onlinebookstore.interswitch.domain;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for accessing Book entities.
 * This interface extends both JpaRepository and JpaSpecificationExecutor to provide
 * standard JPA repository functionality and the ability to use JPA Specifications for querying.
 */
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
}
