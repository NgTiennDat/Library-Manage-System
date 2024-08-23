package com.datien.lms.service.mapper;

import com.datien.lms.dao.Book;
import com.datien.lms.dao.BookTransactionHistory;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.dto.response.BorrowBookResponse;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public BookResponse toBookResponse(Book book) {
        var bookResponse = new BookResponse();
        bookResponse.setTitle(book.getTitle());
        bookResponse.setAuthor(book.getAuthor());
        bookResponse.setPublisher(book.getPublisher());
        bookResponse.setSynopsis(book.getSynopsis());
        bookResponse.setAvailable(book.isAvailable());
        bookResponse.setGenres(book.getGenre());
        return bookResponse;
    }

    public BorrowBookResponse toBorrowBookResponse1(Book book) {
        var borrowBookResponse = new BorrowBookResponse();
        borrowBookResponse.setId(book.getId().intValue());
        borrowBookResponse.setTitle(book.getTitle());
        borrowBookResponse.setAuthor(book.getAuthor());
        borrowBookResponse.setPublisher(book.getPublisher());
        borrowBookResponse.setSynopsis(book.getSynopsis());
//        borrowBookResponse.setReturned(isReturned(book));
//        borrowBookResponse.setReturnApproved(isReturnApproved(book));
        return borrowBookResponse;
    }

    public BorrowBookResponse toBorrowBookResponse(BookTransactionHistory history) {
        var borrowBookResponse = new BorrowBookResponse();
        Book book = history.getBook(); // Lấy đối tượng Book từ BookTransactionHistory

        borrowBookResponse.setId(book.getId().intValue());
        borrowBookResponse.setTitle(book.getTitle());
        borrowBookResponse.setAuthor(book.getAuthor());
        borrowBookResponse.setPublisher(book.getPublisher());
        borrowBookResponse.setSynopsis(book.getSynopsis());
        borrowBookResponse.setReturned(history.isReturned());
        borrowBookResponse.setReturnApproved(history.isReturnApproved());

        return borrowBookResponse;
    }

}
