package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.boundary.bookstore.resource.BookResourceMapper;
import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import com.onlinebookstore.interswitch.domain.specification.BookSpecification;
import com.onlinebookstore.interswitch.shared.cqrs.QueryHandler;
import com.onlinebookstore.interswitch.shared.cqrs.annotations.QueryHandle;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;


/**
 * Handles the BookSearchQuery to retrieve a paginated list of books based on search criteria.
 * This class implements the QueryHandler interface for BookSearchQuery and uses the BookRepository
 * to perform the database query. It also maps the results to BookResource objects.
 * Note: One Optimization that can be done here is to use memcached.
 */
@QueryHandle
@AllArgsConstructor
public class SearchBookQueryHandler implements QueryHandler<BookSearchQuery, Page<BookResource>> {
    private final BookRepository bookRepository;

    @Override
    public Page<BookResource> execute(BookSearchQuery qry) {
        //A more optimised approach is to use memcached for this search,
        //due to time restriction, this can be implemented later.
        Page<Book> bookSearchResult = bookRepository.findAll(BookSpecification.filterBooks(qry.getTitle(),
                        qry.getAuthor(),
                        qry.getYear(),
                        qry.getGenre()),
                        qry.getPageable());

        return BookResourceMapper.toBookResourcePage(bookSearchResult);
    }
}
