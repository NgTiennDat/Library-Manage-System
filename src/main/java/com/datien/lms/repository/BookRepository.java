package com.datien.lms.repository;

import com.datien.lms.dao.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
           SELECT book
           FROM Book book
           WHERE book.available = true
           """)
    Page<Book> findAllByIsAvailableFalse(Pageable pageable);
    ;
}
