package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_book_transaction_history")
public class BookTransactionHistory {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "USER_ID")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID")
    private Book book;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "RETURNED")
    private boolean returned;

    @Column(name = "RETURN_APPROVED")
    private boolean returnApproved;

    @CreatedDate
    @Column(name = "CREATED_AT", insertable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(name = "CREATED_BY")
    private String createdBy;

    @CreatedDate
    @Column(name = "RETURN_DATE")
    private LocalDateTime returnDate;
}
