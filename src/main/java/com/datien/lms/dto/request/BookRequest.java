package com.datien.lms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    private String title;
    private String author;
    private String publisher;
    private String genre;
    private String synopsis;
    private boolean available;
}
