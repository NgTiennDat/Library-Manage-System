package com.datien.lms.repository;

import com.datien.lms.dao.Feedback;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
           SELECT fb
           FROM Feedback fb
           WHERE fb.book.id = :bookId
           """)
    List<Feedback> findAllByBookId(@Param("book-id") Long bookId);
}
