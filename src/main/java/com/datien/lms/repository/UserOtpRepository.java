package com.datien.lms.repository;

import com.datien.lms.dao.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, String> {
    UserOtp findByCode(String code);
    Optional<UserOtp> findByUserId(String userId);
}
