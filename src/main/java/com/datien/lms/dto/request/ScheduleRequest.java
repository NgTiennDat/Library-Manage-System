package com.datien.lms.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequest {

    private Long studentId;
    private Long bookId;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
}
