package com.datien.lms.repository;

import com.datien.lms.dao.BookTransactionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, String> {


    @Query("""
           SELECT bh
           FROM BookTransactionHistory bh
           JOIN User u ON bh.userId = u.id
           WHERE bh.returned = false
           """)
    List<BookTransactionHistory> findAllByReturned();

    @Query("""
           SELECT bh
           FROM BookTransactionHistory bh
           WHERE bh.status = '1'
           """)
    Page<BookTransactionHistory> findAllByStatus(Pageable pageable);

}
