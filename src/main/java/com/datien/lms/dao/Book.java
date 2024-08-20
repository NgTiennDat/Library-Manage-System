package com.datien.lms.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_book")
public class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String author;
    private String publisher;
    private int pageCount;
    private String genre;
    private String synopsis;
    private boolean available;


}
