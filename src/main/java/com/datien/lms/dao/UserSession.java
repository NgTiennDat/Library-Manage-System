package com.datien.lms.dao;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "tbl_user_session")
public class UserSession {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    private String id;

    @Column(name = "SESSION_ID", nullable = false, unique = true)
    private String sessionId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "EXPIRY_TIME", nullable = false)
    private Timestamp expiryTime;

    @Column(name = "DEVICE_ID")
    private String deviceId;

    @Column(name = "STATUS", nullable = false)
    private int status;
}
