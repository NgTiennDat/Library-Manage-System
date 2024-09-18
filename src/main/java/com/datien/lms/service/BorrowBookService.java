package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.Book;
import com.datien.lms.dao.Role;
import com.datien.lms.dao.User;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BorrowBookService {

    private final Logger logger = LogManager.getLogger(BorrowBookService.class);
    private final BookRepository bookRepository;

    public Map<Object, Object> borrowBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            User user = (User) connectedUser.getPrincipal();
            if (user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            if (!book.isAvailable()) {
                result = new Result(ResponseCode.BOOK_NOT_AVAILABLE.getCode(), false, ResponseCode.BOOK_NOT_AVAILABLE.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            book.setAvailable(false);
            bookRepository.save(book);
            notification = "Successfully borrowed book with the Id " + bookId;

            resultExecuted.put(AppConstant.RESPONSE_KEY.DATA, book.getId().intValue());
        } catch (Exception ex) {
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> returnBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            book.setAvailable(true);
            bookRepository.save(book);

        } catch (Exception ex) {
            logger.error("Something errors occur!");
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> returnApproveBook(Long bookId, Authentication connectedUser) {
        Map<Object, Object> resultExecute = new HashMap<>();
        Result result = new Result();

        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new EntityNotFoundException("No book found with the Id " + bookId));

            User user = (User) connectedUser.getPrincipal();

            if(user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }
            book.setAvailable(true);
            bookRepository.save(book);

        } catch (Exception ex) {
            logger.error("Some errors occurs in returnApproveBook", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ex.getMessage());
            resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }
        resultExecute.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecute;
    }

}
