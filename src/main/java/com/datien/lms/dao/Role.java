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
@Table(name = "tbl_role")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    private String name;

    @OneToOne
    private User user;

    @CreatedDate
    @Column(name = "CREATED_AT", nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime lastModifiedAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "LASTMODIFIED_BY")
    private String lastModifiedBy;
}
