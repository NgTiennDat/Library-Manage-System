package com.datien.lms.controller;

import com.datien.lms.dto.request.ScheduleRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createSchedule(
            @RequestBody ScheduleRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(scheduleService.createSchedule(request, connectedUser)));
    }

}
