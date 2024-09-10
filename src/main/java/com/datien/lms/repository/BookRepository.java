package com.datien.lms.repository;

import com.datien.lms.dao.Book;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    String findByISBN = """
            SELECT
                book.title,
                book.author,
                book.publisher,
                book.ISBN,
                book.synopsis,
                book.genre
            FROM Book book
            WHERE book.ISBN = :isbn
            AND book.available = true;
            """;

    @Query("""
           SELECT book
           FROM Book book
           WHERE book.available = true
           """)
    Page<Book> findAllByIsAvailableFalse(Pageable pageable);

    @Query("""
           SELECT book
           FROM Book book
           where book.available = true
           AND book.isDeleted = 'N'
           """)
    Book findByIdAndIsDeleted(Long bookId);

    @Query(nativeQuery = true, value = findByISBN)
    Page<Book> findByISBN(@Param("isbn") String isbn, Pageable pageable);
}
