package com.datien.lms.controller;

import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.dto.response.BorrowBookResponse;
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
    public ResponseEntity<?> getBook(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok(bookService.getAllBook(page, size));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> createBook(
            @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.createBook(bookRequest, connectedUser));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllBookByISBN(
            @RequestParam(name = "ISBN") String ISBN,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.findAllBookByISBN(ISBN, page, size, connectedUser));
    }

    @GetMapping("/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookResponse> getBook(
            @PathVariable("book-id") Long bookId
    ) {
        bookService.getDetailBook(bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/borrowed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Page<BorrowBookResponse>> findAllBorrowedBook(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        bookService.getAllBorrowedBooks(page, size, connectedUser);
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
