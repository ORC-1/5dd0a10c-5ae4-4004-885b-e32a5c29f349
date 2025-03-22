package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.shared.Genre;
import com.onlinebookstore.interswitch.shared.cqrs.Query;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Value
public class BookSearchQuery implements Query<Page<BookResource>> {
    String title;
    String author;
    Integer year;
    Genre genre;
    Pageable pageable;
}
