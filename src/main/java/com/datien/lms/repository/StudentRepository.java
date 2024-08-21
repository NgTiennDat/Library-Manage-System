package com.datien.lms.repository;

import com.datien.lms.dao.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
