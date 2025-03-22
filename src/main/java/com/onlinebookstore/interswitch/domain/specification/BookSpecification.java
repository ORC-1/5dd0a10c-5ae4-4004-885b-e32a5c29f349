package com.onlinebookstore.interswitch.domain.specification;


import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.shared.Genre;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class BookSpecification {
    /**
     * Constructs a JPA Specification to filter books based on various criteria.
     * This method dynamically builds a WHERE clause for database queries using the JPA Criteria API.
     *
     * @param title  Optional title to filter books by (case-insensitive, partial match).
     * @param author Optional author to filter books by (case-insensitive, partial match).
     * @param year   Optional publication year to filter books by.
     * @param genre  Optional genre to filter books by.
     * @return A Specification<Book> object representing the filter criteria.
     * Returns an empty specification (always true) if no filter criteria are provided.
     */
    public static Specification<Book> filterBooks(String title, String author, Integer year, Genre genre) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Add a predicate for param filtering if value is provided.
            if (isNotEmpty(title)) {
                //  Construct a case-insensitive 'like' predicate for partial matching on title.
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (isNotEmpty(author)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), "%" + author.toLowerCase() + "%"));
            }
            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("publicationYear"), year));
            }
            if (genre != null) {
                predicates.add(criteriaBuilder.equal(root.get("genre"), genre));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
