package com.datien.lms.repo;

import com.datien.lms.dao.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
