package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tbl_feedback")
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name = "FEEDBACK_ID")
    private Long id;

    @Column(name = "FEEDBACK_NOTE")
    private Double note;

    @Column(name = "FEEDBACK_DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "book-id")
    private Book book;

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
