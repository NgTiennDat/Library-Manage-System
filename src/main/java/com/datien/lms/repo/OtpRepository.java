package com.datien.lms.repo;

import com.datien.lms.dao.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Otp findByCode(String code);
    Optional<Otp> findByUserId(Long userId);
}
