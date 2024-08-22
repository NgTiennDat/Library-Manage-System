package com.datien.lms.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_BOOK")
public class Book {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR_NAME")
    private String author;

    @Column(name = "PUBLISHER")
    private String publisher;

    @Column(name = "ISBN")
    private String ISBN;

    @Column(name = "PAGE_COUNT")
    private int pageCount;

    @Column(name = "GENRE")
    private String genre;

    @Column(name = "SYNOPSIS")
    private String synopsis;

    @Column(name = "IS_AVAILABLE")
    private boolean available;
}
