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

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(unique = true, insertable = false, name = "CREATED_DATE")
    private LocalDateTime createdDate;
    @Column(name = "MODIFITED_DATE")
    private LocalDateTime modifiedDate;
}
