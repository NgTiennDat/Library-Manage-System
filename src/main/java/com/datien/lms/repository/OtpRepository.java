package com.datien.lms.repository;

import com.datien.lms.dao.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, String> {
    Otp findByCode(String code);
    Optional<Otp> findByUserId(String userId);
}
