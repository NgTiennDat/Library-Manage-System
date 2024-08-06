package com.datien.lms.user.repo;

import com.datien.lms.user.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
