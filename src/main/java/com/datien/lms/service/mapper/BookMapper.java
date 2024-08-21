package com.datien.lms.service.mapper;

import com.datien.lms.dao.Book;
import com.datien.lms.dto.response.BookResponse;
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
}
