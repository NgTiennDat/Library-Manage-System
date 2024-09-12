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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final Logger logger = LogManager.getLogger(ScheduleService.class);
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;

    private boolean isAuthorized(User user) {
        return user.getRole() != null && user.getRole() != Role.STUDENT;
    }

    public Map<Object, Object> createSchedule(ScheduleRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");

        try {
            User user = (User) connectedUser.getPrincipal();
            if (!isAuthorized(user)) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
            Optional<User> optionalStudent = userRepository.findById(request.getStudentId());

            if (optionalBook.isEmpty()) {
                result = new Result(ResponseCode.BOOK_NOT_FOUND.getCode(), false, ResponseCode.BOOK_NOT_FOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            if (optionalStudent.isEmpty()) {
                result = new Result(ResponseCode.USER_NOTFOUND.getCode(), false, ResponseCode.USER_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            Schedule schedule = new Schedule();
            schedule.setStudent(optionalStudent.get());
            schedule.setBook(optionalBook.get());
            schedule.setBorrowDate(request.getBorrowDate());
            schedule.setIsDeleted(request.getIsDelete());
            schedule.setDueDate(request.getDueDate());
            schedule.setStatus(ScheduleStatus.valueOf(request.getStatus()));
            schedule.setCreatedBy(user.getId());
            schedule.setCreatedDate(LocalDateTime.now());
            scheduleRepository.save(schedule);

        } catch (Exception ex) {
            logger.error("Some errors occurred while creating schedule", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        }

        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        return resultExecuted;
    }

    public Map<Object, Object> getScheduleById(Long id, Authentication connectedUser) {

        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var user = (User) connectedUser.getPrincipal();
            if(user.getRole() == null || user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var schedule = scheduleRepository.findByIdAndIsDeleted(id);
            if(schedule == null) {
                result = new Result(ResponseCode.SCHEDULE_NOTFOUND.getCode(), false, ResponseCode.SCHEDULE_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            }

            notification = "Get schedule information successfully.";

        } catch (Exception ex) {
            logger.error("Some errors occurred while getting schedule by id", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Get schedule information failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> updateScheduleStatus(Long id, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";

        try {
            var user = (User) connectedUser.getPrincipal();
            if(user.getRole() == null || user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                notification = "Access denied. Only authorized roles can update schedule status.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            var schedule = scheduleRepository.findByIdAndIsDeleted(id);
            if(schedule == null) {
                result = new Result(ResponseCode.BOOK_NOT_FOUND.getCode(), false, ResponseCode.BOOK_NOT_FOUND.getMessage());
                notification = "Schedule not found.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            if(schedule.getStatus() == ScheduleStatus.RETURNED) {
                notification = "The book have been returned so you cannot update the schedule.";
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
                return resultExecuted;
            }

            if(schedule.getStatus() == ScheduleStatus.BORROWED) {
                if(schedule.getDueDate().isBefore(LocalDateTime.now())) {
                    schedule.setStatus(ScheduleStatus.OVERDUE);
                    notification = "You need to return book immediately.";
                }
            }

            if(schedule.getDueDate().isAfter(LocalDateTime.now().plusDays(5))) {
                user.setEnabled(false);
                userRepository.save(user);
                notification = "Your account is locked and you are summoned to the training management office for processing.";
            } else {
                notification = "Update schedule information successfully.";
            }

        } catch (Exception ex) {
            logger.error("Some errors occurred while updating schedule status", ex);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Update schedule status failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> updateSchedule(Long id, ScheduleRequest request, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";
        try {

            var user = (User) connectedUser.getPrincipal();
            if(user.getRole() == null) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var schedule = scheduleRepository.findByIdAndIsDeleted(id);
            if(schedule == null) {
                result = new Result(ResponseCode.SCHEDULE_NOTFOUND.getCode(), false, ResponseCode.SCHEDULE_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            schedule.setDueDate(request.getDueDate());
            schedule.setStatus(ScheduleStatus.valueOf(request.getStatus()));
            schedule.setBorrowDate(request.getBorrowDate());
            schedule.setUpdatedDate(LocalDateTime.now());
            schedule.setUpdatedBy(user.getId());
            scheduleRepository.save(schedule);
            notification = "Update schedule successfully.";

        } catch (Exception e) {
            logger.error("Some errors occurred while updating schedule", e);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Update schedule info failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }

    public Map<Object, Object> deleteSchedule(Long id, Authentication connectedUser) {
        Map<Object, Object> resultExecuted = new HashMap<>();
        Result result = Result.OK("");
        String notification = "";
        try {
            var user = (User) connectedUser.getPrincipal();
            if(user.getRole() == null || user.getRole() == Role.STUDENT) {
                result = new Result(ResponseCode.ACCESS_DENIED.getCode(), false, ResponseCode.ACCESS_DENIED.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            var schedule = scheduleRepository.findByIdAndIsDeleted(id);
            if(schedule == null) {
                result = new Result(ResponseCode.SCHEDULE_NOTFOUND.getCode(), false, ResponseCode.SCHEDULE_NOTFOUND.getMessage());
                resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
                return resultExecuted;
            }

            schedule.setIsDeleted("Y");
            scheduleRepository.save(schedule);
            notification = "Delete schedule successfully.";

        } catch (Exception e) {
            logger.error("Some errors occurred while updating schedule", e);
            result = new Result(ResponseCode.SYSTEM.getCode(), false, ResponseCode.SYSTEM.getMessage());
            resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
            notification = "Delete schedule info failed.";
            resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        }
        resultExecuted.put(AppConstant.RESPONSE_KEY.RESULT, result);
        resultExecuted.put(AppConstant.RESPONSE_KEY.NOTIFICATION, notification);
        return resultExecuted;
    }
}
