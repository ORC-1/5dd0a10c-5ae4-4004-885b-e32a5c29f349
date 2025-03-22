package com.onlinebookstore.interswitch.boundary.bookstore.resource;

import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.domain.Book;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;


public class BookResourceMapper {
    public static Page<BookResource> toBookResourcePage(Page<Book> bookPage) {
        List<BookResource> bookResources = bookPage.getContent().stream()
                .map(BookResourceMapper::toBookResource)
                .collect(Collectors.toList());
        return new PageImpl<>(bookResources, bookPage.getPageable(), bookPage.getTotalElements());
    }

    private static BookResource toBookResource(Book book) {
        BookResource resource = new BookResource();
        resource.setId(book.getId());
        resource.setTitle(book.getTitle());
        resource.setGenre(book.getGenre());
        resource.setIsbn(book.getIsbn());
        resource.setAuthor(book.getAuthor());
        resource.setPublicationYear(book.getPublicationYear());
        return resource;
    }
}
