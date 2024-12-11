package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Feedback;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.FeedbackDto;
import com.datien.lms.dto.request.model.FeedbackRequest;
import com.datien.lms.dto.response.FeedbackResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final Logger logger = LogManager.getLogger(FeedbackService.class);
    private final BookRepository bookRepository;
    private final FeedbackRepository feedbackRepository;

    public Map<Object, Object> createFeedback(FeedbackRequest request, String bookId, Authentication connectedUser) {

        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book exist with the Id: " + bookId));

            var newFeedback = new Feedback();
            String feedbackId = UUID.randomUUID().toString();
            newFeedback.setId(feedbackId);
            newFeedback.setNote(request.getNote());
            newFeedback.setDescription(request.getDescription());
            newFeedback.setBook(book);
            newFeedback.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            newFeedback.setIsDeleted(AppConstant.STATUS.IS_UN_DELETED);
            newFeedback.setCreatedBy(user.getId());
            feedbackRepository.save(newFeedback);

            notification = "Add feedback successfully.";
        } catch (Exception e) {
            logger.error("Some errors occurred while creating the feedback", e);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Add feedback failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> getAllFeedbackByBookId(String bookId, int pageSize, int pageNumber, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();
        String notification = "";
        FeedbackResponse feedbackResponse = new FeedbackResponse();

        try {
            var book = bookRepository.findById(bookId);

            if(book.isEmpty()) {
                result = new Result(ResponseCode.BOOK_NOT_FOUND.getCode(), false, ResponseCode.BOOK_NOT_FOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                notification = "You don't have permission to access.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<Feedback> feedbackPage = feedbackRepository.findAllByBookId(bookId, pageable);

            List<FeedbackDto> feedbackDtos = feedbackPage.getContent().stream()
                    .map(feedback -> {
                        FeedbackDto feedbackDto = new FeedbackDto();
                        feedbackDto.setNote(feedback.getNote());
                        feedbackDto.setDescription(feedback.getDescription());
                        feedbackDto.setCreatedAt(feedback.getCreatedAt());
                        feedbackDto.setCreatedBy(user.getId());
                        return feedbackDto;
                    }).toList();

            feedbackResponse.setFeedbackDtos(feedbackDtos);
            feedbackResponse.setTotalRecords((int) feedbackPage.getTotalElements());
            notification = "Get feedback of the book successfully.";

        } catch (Exception ex) {
            logger.error("Some errors occurred while getting the feedback", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Get feedback of the book failed.";
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, feedbackResponse);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }


    public Map<Object, Object> updateDetailedFeedback(String feedbackId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Optional<Feedback> feedbackOptional = feedbackRepository.findById(feedbackId);
            if (feedbackOptional.isEmpty()) {
                result = new Result(ResponseCode.FEEDBACK_NOTFOUND.getCode(), false, ResponseCode.FEEDBACK_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Feedback feedback = feedbackOptional.get();

            feedbackRepository.save(feedback);
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, feedback);

        } catch (Exception ex) {
            logger.error("Some errors occurred while updating the feedback", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }

    public Map<Object, Object> deleteFeedback(String feedbackId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

        try {
            var feedback = feedbackRepository.findById(feedbackId)
                    .orElseThrow(() -> new RuntimeException(ResponseCode.FEEDBACK_NOTFOUND.getCode()));
            feedback.setIsDeleted(AppConstant.STATUS.IS_DELETED);
            feedbackRepository.save(feedback);

        } catch (Exception ex) {
            logger.error("Some errors occurred while deleting the feedback", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }
}
