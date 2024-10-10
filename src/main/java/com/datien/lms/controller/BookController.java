package com.datien.lms.controller;

import com.datien.lms.dto.request.BookRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.BookService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return ResponseEntity.ok(ResponseData.createResponse(bookService.getAllBook(page, size)));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('admin::create')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBook(
            @RequestBody BookRequest bookRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.createBook(bookRequest, connectedUser)));
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> getAllBookByISBN(
            @RequestParam(name = "ISBN") String ISBN,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.findAllBookByISBN(ISBN, page, size, connectedUser)));
    }

    @GetMapping("/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> getBook(
            @PathVariable("book-id") String bookId
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.getDetailBook(bookId)));
    }

    @GetMapping("/borrowed")
    @PreAuthorize("hasAnyAuthority('admin::read')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> findAllBorrowedBook(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getAllBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("/returned")
    @PreAuthorize("hasAnyAuthority('admin::read')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> findAllReturnedBook(
            @RequestParam(name = "page", defaultValue = "10", required = false) int page,
            @RequestParam(name = "size", defaultValue = "0", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.getAllReturnedBooks(page, size, connectedUser)));
    }

    @DeleteMapping("/{book-id}")
    @PreAuthorize("hasAnyAuthority('admin::delete')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> deleteBook(
            @PathVariable("book-id") String bookId,
            @RequestParam("isDeleted") String isDeleted,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.deleteBook(bookId, isDeleted, connectedUser)));
    }

    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('admin::create')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") String bookId,
            @Parameter()
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(bookService.uploadBookCoverPicture (file, connectedUser, bookId)));
    }

}
