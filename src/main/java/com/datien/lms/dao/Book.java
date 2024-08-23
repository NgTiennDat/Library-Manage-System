package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

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

    @Column(name = "IS_ARCHIVED")
    private boolean archived;

    @CreatedDate
    @Column(name = "CREATED_AT", insertable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime lastModifiedAt;

    @CreatedDate
    @Column(name = "CREATED_BY")
    private String createdBy;

    @LastModifiedDate
    @Column(name = "MODIFIED_BY")
    private String lastModifiedBy;

}
