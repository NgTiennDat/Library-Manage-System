package com.datien.lms.repository;

import com.datien.lms.dao.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    String findByISBN = """
            SELECT
                book.title,
                book.author,
                book.publisher,
                book.ISBN,
                book.synopsis,
                book.genre,
                user.userId
            FROM Book book
            INNER JOIN User user ON book.userId = user.id
            WHERE book.ISBN = :isbn
            AND book.available = true;
            """;

    @Query("""
           SELECT book
           FROM Book book
           WHERE book.available = true
           """)
    Page<Book> findAllByIsAvailableFalse(Pageable pageable);

    @Query(nativeQuery = true, value = findByISBN)
    Page<Book> findByISBN(String isbn, Pageable pageable, Long userId);
    ;
}
