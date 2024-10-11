package com.datien.lms.repository;

import com.datien.lms.dao.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    String insertSql
            = "INSERT INTO tbl_user_session(ID, USER_ID, SESSION_ID, DEVICE_ID, EXPIRY_TIME, STATUS) " +
            "VALUES (:id, :userID, :sessionId, :deviceId, :expiryTime, :status)";

    List<UserSession> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") int status);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = insertSql)
    int insert(@Param("id") String id, @Param("userID") String userId,
               @Param("sessionId") String sessionId, @Param("deviceId") String deviceId,
               @Param("expiryTime") Date expiryTime, @Param("status") int status);
}
