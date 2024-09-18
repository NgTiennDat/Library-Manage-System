package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Token {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "EXPRIED")
    private boolean expired;

    @Column(name = "REVOKED")
    private boolean revoked;

    @Column(name = "TOKEN_TYPE")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "CREATED_AT", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "EXPIRED_AT")
    private LocalDateTime expiredAt;

    @Column(name = "VALIDATED_AT")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id", nullable = false
    )
    private User user;
}
