package com.onlinebookstore.interswitch.application.query;

import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.domain.Book;
import com.onlinebookstore.interswitch.domain.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchBookQueryHandlerTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private SearchBookQueryHandler queryHandler;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void ShouldReturnMappedBookResourcesWhenBooksAreFound() {
        // Given
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");

        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        doReturn(bookPage).when(bookRepository).findAll(any(Specification.class), any(Pageable.class));

        // When
        Page<BookResource> result = queryHandler.execute(new BookSearchQuery("Test Book", "Test Author", null, null, pageable));

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void ShouldReturnEmptyPageWhenNoBooksAreFound() {
        // Given
        Page<Book> emptyBookPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        doReturn(emptyBookPage).when(bookRepository).findAll(any(Specification.class), any(Pageable.class));

        // When
        Page<BookResource> result = queryHandler.execute(new BookSearchQuery("Non-existent", null, null, null, pageable));

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void ShouldHandleNullSearchParameters() {
        // Given
        Page<Book> bookPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        doReturn(bookPage).when(bookRepository).findAll(any(Specification.class), any(Pageable.class));

        // When
        Page<BookResource> result = queryHandler.execute(new BookSearchQuery(null, null, null, null, pageable));

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }
}
