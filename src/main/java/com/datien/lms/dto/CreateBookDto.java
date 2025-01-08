package com.datien.lms.dto;

import lombok.Data;

@Data
public class CreateBookDto {

    private String id;
    private String title;
    private String author;
    private String publisher;
    private String ISBN;
    private String synopsis;
    private int pageCount;
    private String genre;
    private boolean available;
    private boolean archived;
    private String bookCover;

}
