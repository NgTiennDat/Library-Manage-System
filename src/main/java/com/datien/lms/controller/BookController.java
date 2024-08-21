package com.datien.lms.controller;

import com.datien.lms.dto.response.BookResponse;
import com.datien.lms.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v2/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("")
    public ResponseEntity<BookResponse> getBook(
            Authentication connectedUser
    ) {
        bookService.getAllBook(connectedUser);
        return ResponseEntity.ok().build();
    }
}
