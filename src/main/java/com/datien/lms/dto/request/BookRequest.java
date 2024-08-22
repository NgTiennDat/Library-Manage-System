package com.datien.lms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String title;
    private String author;
    private String publisher;
    private String ISBN;
    private String synopsis;
    private int pageCount;
    private String genre;
    private boolean available;
}
