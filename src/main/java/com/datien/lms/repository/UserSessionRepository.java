package com.datien.lms.repository;

import com.datien.lms.dao.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    UserSession findByUserIdAndStatus(@Param("userId") String userId, @Param("status") int status);
}
