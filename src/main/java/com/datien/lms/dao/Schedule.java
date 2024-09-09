package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tbl_schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE", nullable = false)
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(name = "MODIFIED_AT")
    private LocalDateTime lastModifiedAt;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "MODIFIED_BY")
    private String lastModifiedBy;

    @Column(name = "IS_DELETED", length = 1, nullable = false)
    private String isDeleted;
}
