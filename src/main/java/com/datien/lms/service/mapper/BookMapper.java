package com.datien.lms.service.mapper;

import com.datien.lms.dao.Book;
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

    public BorrowBookResponse toBorrowBookResponse(Book book) {
        return null;
    }
}
