package com.datien.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    @NotBlank(message = "Title is mandatory.")
    private String title;

    @NotBlank(message = "Author is mandatory.")
    private String author;

    @NotBlank(message = "Publisher is mandatory.")
    private String publisher;

    @NotBlank(message = "ISBN is mandatory.")
    private String ISBN;

    @NotBlank(message = "Synopsis is mandatory.")
    private String synopsis;

    @NotNull(message = "Page count is mandatory.")
    private int pageCount;

    @NotBlank(message = "Genre is mandatory.")
    private String genre;

    @NotBlank(message = "IsDelete is mandatory")
    private String isDelete;

    @NotNull(message = "Availability status is mandatory.")
    private boolean available;

    @NotNull(message = "Archived status is mandatory")
    private boolean archived;
}
