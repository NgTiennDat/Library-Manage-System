package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_book")
public class Book {
    @Id
    @Column(name = "ID")
    private String id;

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

    @Column(name = "BOOK_COVER")
    private String bookCover;

    @Column(name = "IS_AVAILABLE")
    private boolean available;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "IS_ARCHIVED")
    private boolean archived;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "MODIFIED_AT")
    private LocalDateTime lastModifiedAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "LIST_OF_FEEDBACK")
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        return Math.round(rate * 10.0) / 10.0;
    }

}
