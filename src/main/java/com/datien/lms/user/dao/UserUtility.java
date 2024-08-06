package com.datien.lms.user.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class UserUtility {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    private String firstName;
    private String lastName;

    private String createdBy;

    @Column(unique = true, nullable = false)
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
