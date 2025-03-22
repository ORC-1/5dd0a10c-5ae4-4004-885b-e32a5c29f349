package com.onlinebookstore.interswitch.boundary.bookstore;

import com.onlinebookstore.interswitch.boundary.bookstore.response.BookResource;
import com.onlinebookstore.interswitch.shared.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequestMapping(BookInventoryController.BASE_PATH)
public interface BookInventoryController {
    String BASE_PATH = "api/books";

    @GetMapping("/search")
    Page<BookResource> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Genre genre,
            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable);
}
