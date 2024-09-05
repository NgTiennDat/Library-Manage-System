package com.datien.lms.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
public class FeedbackDto {

    @Column(name = "FEEDBACK_NOTE")
    private Double note;

    @Column(name = "FEEDBACK_DESCRIPTION")
    private String description;

    @CreationTimestamp
    @Column(name = "CREATED_AT", insertable = false)
    private Timestamp createdAt;

    @Column(name = "CREATED_BY")
    private Long createdBy;
}
