package com.datien.lms.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookResponse {
    private String title;
    private String author;
    private String publisher;
    private String ISBN;
    private String synopsis;
    private boolean available;
    private String genres;
}
