package com.datien.lms.service;

import com.datien.lms.common.AppConstant;
import com.datien.lms.common.Result;
import com.datien.lms.dao.*;
import com.datien.lms.dto.request.ScheduleRequest;
import com.datien.lms.handlerException.ResponseCode;
import com.datien.lms.repository.BookRepository;
import com.datien.lms.repository.ScheduleRepository;
import com.datien.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final Logger logger = LogManager.getLogger(ScheduleService.class);
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    public Map<Object, Object> createSchedule(ScheduleRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if(user.getRole() == null || user.getRole() != Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Book book = bookRepository.findById(request.getBookId()).orElse(null);
            User student = userRepository.findById(request.getStudentId()).orElse(null);

            if (book == null) {
                result = new Result(ResponseCode.BOOK_NOT_FOUND.getCode(), false, ResponseCode.BOOK_NOT_FOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            if (student == null) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Schedule schedule = new Schedule();
            schedule.setStudent(student);
            schedule.setBook(book);
            schedule.setBorrowDate(request.getBorrowDate());
            schedule.setDueDate(request.getDueDate());
            schedule.setStatus(ScheduleStatus.PENDING);

            scheduleRepository.save(schedule);

        } catch (Exception ex) {
            logger.error("Some errors occurred while creating schedule", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

}
