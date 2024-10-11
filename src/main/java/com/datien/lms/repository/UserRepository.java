package com.datien.lms.repository;

import com.datien.lms.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("""
            SELECT u
            From User u
            WHERE u.email = :email
            AND u.isDeleted = :isDeleted
            """)
    User findByEmailAndIsDeleted(String email, String isDeleted);
}
