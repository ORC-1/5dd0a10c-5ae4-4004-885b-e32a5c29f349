package com.onlinebookstore.interswitch.api.bookstore;

import com.onlinebookstore.interswitch.application.query.BookSearchQuery;
import com.onlinebookstore.interswitch.boundary.bookstore.BookInventoryController;
import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.shared.Genre;
import com.onlinebookstore.interswitch.shared.cqrs.QueryGate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RequiredArgsConstructor
@RestController
public class BookInventoryControllerImpl implements BookInventoryController {
    private final QueryGate queryGate;

    @Override
    public Page<BookResource> searchBooks(String title, String author, Integer year, Genre genre, Pageable pageable) {
        if (title == null && author == null && year == null && genre == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one search parameter is required.");

        return queryGate.handle(new BookSearchQuery(title, author, year, genre, pageable));
    }
}
