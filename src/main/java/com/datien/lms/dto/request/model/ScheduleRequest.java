package com.datien.lms.dto.request.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleRequest {

    @NotBlank(message = "Student id is mandatory.")
    private String studentId;
    @NotBlank(message = "Book id is mandatory.")
    private String bookId;
    @NotBlank(message = "Status is mandatory.")
    private String status;
    @NotBlank(message = "Borrow date is mandatory.")
    private LocalDateTime borrowDate;
    @NotBlank(message = "Due date is mandatory.")
    private LocalDateTime dueDate;
    @NotBlank(message = "Is deleted is mandatory.")
    private String isDelete;
}
