package com.datien.lms.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String synopsis;
    private String genres;
    private boolean available;
}
