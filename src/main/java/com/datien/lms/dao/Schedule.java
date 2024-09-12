package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_schedule")
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @Column(name = "BORROW_DATE", nullable = false)
    private LocalDateTime borrowDate;

    @Column(name = "DUE_DATE", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "RETURN_DATE")
    private LocalDateTime returnDate;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;


}
