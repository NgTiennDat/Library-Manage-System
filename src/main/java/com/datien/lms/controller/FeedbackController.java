package com.datien.lms.controller;

import com.datien.lms.dto.request.model.FeedbackRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> createFeedback(
            @RequestBody @Valid FeedbackRequest request,
            @RequestParam(name = "bookId", defaultValue = "") String bookId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(feedbackService.createFeedback(request, bookId, connectedUser)));
    }

    @GetMapping("{book-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> getAllFeedbackByBookId(
            @PathVariable("book-id") String bookId,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(feedbackService.getAllFeedbackByBookId(bookId, pageSize, pageNumber, connectedUser)));
    }

    @PatchMapping("/update/{feedback-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateDetailFeedback(
            @PathVariable("feedback-id") String feedbackId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(feedbackService.updateDetailedFeedback(feedbackId, connectedUser)));
    }

    @DeleteMapping("{feedback-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> deleteFeedback(
            @PathVariable("feedback-id") String feedbackId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(feedbackService.deleteFeedback(feedbackId, connectedUser)));
    }
}
