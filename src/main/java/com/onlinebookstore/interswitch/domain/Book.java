package com.onlinebookstore.interswitch.domain;

import com.onlinebookstore.interswitch.shared.Genre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

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

    @CreationTimestamp
    private Date createdAt;
}
