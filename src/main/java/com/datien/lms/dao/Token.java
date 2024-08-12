package com.datien.lms.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Token {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPRIED")
    private boolean expired;

    @Column(name = "REVOKED")
    private boolean revoked;

    @Column(name = "CREATED_AT", nullable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    @Column(name = "VALIDATED_AT")
    private LocalDateTime validatedAt;
}
