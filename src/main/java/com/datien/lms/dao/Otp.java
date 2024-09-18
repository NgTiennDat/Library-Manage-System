package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_otp")
public class Otp {
    @Id
    @Column(name = "OTP_ID")
    private String id;

    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime validatedAt;
    private String username;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

}