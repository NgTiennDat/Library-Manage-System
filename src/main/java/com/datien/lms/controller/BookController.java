package com.datien.lms.controller;

import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/book")
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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBook(
            @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.createBook(bookRequest, connectedUser));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
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
    public ResponseEntity<?> getBook(
            @PathVariable("book-id") Long bookId
    ) {
        return ResponseEntity.ok(bookService.getDetailBook(bookId));
    }

    @GetMapping("/borrowed")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> findAllBorrowedBook(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> findAllReturnedBook(
            @RequestParam(name = "page", defaultValue = "10", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getAllReturnedBooks(page, size, connectedUser));
    }


    @PostMapping("/borrowed/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> borrowBook(
            @PathVariable("book-id") Long bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.borrowBook(bookId, connectedUser));
    }

    @PatchMapping("/borrowed/returned/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> returnBook(
            @PathVariable("book-id") Long bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.returnBook(bookId, connectedUser));
    }

    @PatchMapping("/borrowed/returned/approved/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> approveBook(
            @PathVariable("book-id") Long bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.returnApproveBook(bookId, connectedUser));
    }

    @DeleteMapping("/{book-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBook(
            @PathVariable("book-id") Long bookId,
            @RequestParam("hardDelete") boolean hardDelete,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.deleteBook(bookId, hardDelete, connectedUser));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Long bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.uploadBookCoverPicture (file, connectedUser, bookId));
    }

}
