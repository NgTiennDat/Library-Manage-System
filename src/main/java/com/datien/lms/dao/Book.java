package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_book")
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

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "IS_ARCHIVED")
    private boolean archived;

    @CreationTimestamp
    @Column(name = "CREATED_AT", insertable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime lastModifiedAt;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "MODIFIED_BY")
    private String lastModifiedBy;

}
