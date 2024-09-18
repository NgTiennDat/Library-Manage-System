package com.datien.lms.repository;

import com.datien.lms.dao.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Query("""
           SELECT s
           FROM Schedule s
           WHERE s.isDeleted = 'N'
           """)
    Schedule findByIdAndIsDeleted(String id);
}
