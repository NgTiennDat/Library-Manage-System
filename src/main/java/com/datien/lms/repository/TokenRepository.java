package com.datien.lms.repository;

import com.datien.lms.dao.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, String> {
    Optional<Token> findByToken(String token);

    @Query("""
           SELECT t
           FROM Token t
           INNER JOIN User u on t.user.id = u.id
           WHERE u.id = :userId
           AND (t.expired = false or t.revoked = false)
           """)
    List<Token> findAllValidTokenByUser(String userId);
}
