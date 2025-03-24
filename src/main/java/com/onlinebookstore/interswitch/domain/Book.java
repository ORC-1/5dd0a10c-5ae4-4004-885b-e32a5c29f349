package com.onlinebookstore.interswitch.domain;

import com.onlinebookstore.interswitch.shared.Genre;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Title must contain only letters and numbers.")
    @NotNull
    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    @NotBlank
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only numbers and dashes(-).")
    @NotNull
    private String isbn;

    @NotBlank
    @NotNull
    private String author;

    @Min(value = 1000, message = "Year must be a valid 4-digit number.")
    @Max(value = 9999, message = "Year must be a valid 4-digit number.")
    @NotNull
    private int publicationYear;

    @NotNull(message = "Price must not be null.")
    @Positive(message = "Price must be greater than zero.")
    private BigDecimal price;

    @NotNull(message = "Stock quantity must not be null.")
    @Min(value = 1, message = "Stock quantity cannot be negative.")
    private int stockQuantity;

    @CreationTimestamp
    private Date createdAt;
}
