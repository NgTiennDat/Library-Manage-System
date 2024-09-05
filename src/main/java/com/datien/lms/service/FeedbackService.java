package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Feedback;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.dto.FeedbackDto;
import com.datien.lms.dto.request.FeedbackRequest;
import com.datien.lms.dto.response.FeedbackResponse;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.repository.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    public Map<Object, Object> createFeedback(FeedbackRequest request, Long bookId, Authentication connectedUser) {

        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

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
            newFeedback.setNote(request.getNote());
            newFeedback.setDescription(request.getDescription());
            newFeedback.setBook(book);
            newFeedback.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            newFeedback.setCreatedBy(user.getId());

            feedbackRepository.save(newFeedback);

        } catch (Exception e) {
            logger.error("Some errors occurred while creating the feedback", e);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> getAllFeedbackByBookId(Long bookId, int pageSize, int pageNumber, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

        FeedbackResponse feedbackResponse = new FeedbackResponse();
        List<FeedbackDto> feedbackDtos = new ArrayList<>();

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
            }

            List<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId);

            feedbackDtos = feedbacks.stream()
                    .map(feedback -> {
                        FeedbackDto feedbackDto = new FeedbackDto();
                        feedbackDto.setNote(feedback.getNote());
                        feedbackDto.setDescription(feedback.getDescription());
                        feedbackDto.setCreatedAt(feedback.getCreatedAt());
                        feedbackDto.setCreatedBy(user.getId());
                        return feedbackDto;
                    }).toList();

            List<FeedbackDto> paginatedList = feedbackDtos.stream()
                    .skip((long) pageSize * pageNumber)
                    .limit(pageSize)
                    .toList();

            feedbackResponse.setFeedbackDtos(paginatedList);
            feedbackResponse.setTotalRecords(feedbackDtos.size());

        } catch (Exception ex) {
            logger.error("Some errors occurred while getting the feedback", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, feedbackResponse);
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> updateDetailedFeedback(Long feedbackId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = new Result();

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            // Fetch the feedback by ID
            Optional<Feedback> feedbackOptional = feedbackRepository.findById(feedbackId);
            if (!feedbackOptional.isPresent()) {
                result = new Result(ResponseCode.FEEDBACK_NOTFOUND.getCode(), false, ResponseCode.FEEDBACK_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Feedback feedback = feedbackOptional.get();

            // Update feedback details
            feedbackRepository.save(feedback);

            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            resultExecuted.put("feedback", feedback);

        } catch (Exception ex) {
            logger.error("Some errors occurred while updating the feedback", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        return resultExecuted;
    }

}
