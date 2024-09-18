package com.datien.lms.repository;

import com.datien.lms.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT u
            From User u
            WHERE u.isDeleted = 'N'
            AND u.email = :email
            """)
    Optional<User> findByEmail(String email);
}
