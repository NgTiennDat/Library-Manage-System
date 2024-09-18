package com.datien.lms.controller;

import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.BorrowBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class BorrowBookController {

    private final BorrowBookService borrowBookService;

    @PostMapping("/borrowed/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> borrowBook(
            @PathVariable("book-id") String bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(borrowBookService.borrowBook(bookId, connectedUser)));
    }

    @PatchMapping("/borrowed/returned/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> returnBook(
            @PathVariable("book-id") String bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(borrowBookService.returnBook(bookId, connectedUser)));
    }

    @PatchMapping("/borrowed/returned/approved/{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> approveBook(
            @PathVariable("book-id") String bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(borrowBookService.returnApproveBook(bookId, connectedUser)));
    }

}
