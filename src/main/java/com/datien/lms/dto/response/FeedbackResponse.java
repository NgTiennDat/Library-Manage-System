package com.datien.lms.dto.response;

import com.datien.lms.dto.FeedbackDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FeedbackResponse {
    private List<FeedbackDto> feedbackDtos;
    private int totalRecords;
}
