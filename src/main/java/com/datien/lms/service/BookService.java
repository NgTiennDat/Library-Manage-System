package com.datien.lms.service;

import com.datien.lms.dao.Book;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.repo.BookRepository;
import com.datien.lms.service.mapper.BookMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public void createBook(
            BookRequest bookRequest,
            Authentication connectedUser
    ) {
        User user = (User) connectedUser;
        if(user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You dont have permission to add book to the library");
        }

        var book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPublisher(bookRequest.getPublisher());
        book.setSynopsis(bookRequest.getSynopsis());
        book.setGenre(bookRequest.getGenre());
        book.setAvailable(true);
        bookRepository.save(book);
    }

    public Page<BookResponse> getAllBook(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Book> books = bookRepository.findAll(pageable);
        List<BookResponse> bookResponses = books
                .stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(bookResponses, pageable, books.getTotalElements());
    }

    public BookResponse getDetailBook(Long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));
        return bookMapper.toBookResponse(book);
    }

    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }
}
