package com.onlinebookstore.interswitch.domain;

import com.onlinebookstore.interswitch.shared.Genre;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidationWhenValidBook() {
        Book book = new Book(
                UUID.randomUUID(),
                "Valid Title123",
                Genre.FICTION,
                "1234-5678-9012",
                "John Doe",
                2023,
                new BigDecimal("19.99"),
                10,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertTrue(violations.isEmpty(), "Book should be valid but found violations: " + violations);
    }

    @Test
    void shouldFailValidationWhenTitleHasInvalidCharacters() {
        Book book = new Book(
                UUID.randomUUID(),
                "Invalid@Title!",
                Genre.FICTION,
                "1234-5678-9012",
                "John Doe",
                2023,
                new BigDecimal("19.99"),
                10,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Title must contain only letters and numbers.")));
    }

    @Test
    void shouldFailValidationWhenISBNHasInvalidCharacters() {
        Book book = new Book(
                UUID.randomUUID(),
                "Valid Title",
                Genre.FICTION,
                "1234/5678",
                "John Doe",
                2023,
                new BigDecimal("19.99"),
                10,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("ISBN must contain only numbers and dashes(-).")));
    }

    @Test
    void shouldFailValidationWhenPublicationYearIsInvalid() {
        Book book = new Book(
                UUID.randomUUID(),
                "Valid Title",
                Genre.FICTION,
                "1234-5678-9012",
                "John Doe",
                999,
                new BigDecimal("19.99"),
                10,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Year must be a valid 4-digit number.")));
    }

    @Test
    void shouldFailValidationWhenPriceIsNegative() {
        Book book = new Book(
                UUID.randomUUID(),
                "Valid Title",
                Genre.FICTION,
                "1234-5678-9012",
                "John Doe",
                2023,
                new BigDecimal("-5.00"),
                10,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Price must be greater than zero.")));
    }

    @Test
    void shouldFailValidationWhenStockQuantityIsNegative() {
        Book book = new Book(
                UUID.randomUUID(),
                "Valid Title",
                Genre.FICTION,
                "1234-5678-9012",
                "John Doe",
                2023,
                new BigDecimal("19.99"),
                -5,
                null
        );

        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Stock quantity cannot be negative.")));
    }
}
