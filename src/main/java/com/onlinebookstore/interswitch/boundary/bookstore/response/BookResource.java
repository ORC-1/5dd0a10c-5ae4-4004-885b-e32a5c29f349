package com.onlinebookstore.interswitch.boundary.bookstore.response;

import com.onlinebookstore.interswitch.shared.Genre;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Data;


@Data
public class BookResource {
    private UUID id;
    private String title;
    private Genre genre;
    private String isbn;
    private String author;
    private BigDecimal price;
    private int stockQuantity;
    private int publicationYear;
}
