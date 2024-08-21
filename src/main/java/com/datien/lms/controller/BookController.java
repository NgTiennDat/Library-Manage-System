package com.datien.lms.controller;

import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v2/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Page<BookResponse>> getBook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        bookService.getAllBook(page, size);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookResponse> createBook(
            @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ) {
        bookService.createBook(bookRequest, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookResponse> getBook(
            @PathVariable("book-id") Long bookId
    ) {
        bookService.getDetailBook(bookId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{book-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBook(
            @PathVariable("book-id") Long bookId
    ) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }
}
