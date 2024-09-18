package com.datien.lms.repository;

import com.datien.lms.dao.Feedback;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    @Query("""
       SELECT fb
       FROM Feedback fb
       WHERE fb.book.id = :bookId
       """)
    Page<Feedback> findAllByBookId(@Param("bookId") String bookId, Pageable pageable);

}
