package com.datien.lms.controller;

import com.datien.lms.dto.request.model.ScheduleRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestBody @Valid ScheduleRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(scheduleService.createSchedule(request, connectedUser)));
    }

    @GetMapping("/{schedule-id}")
    @PreAuthorize("hasAuthority('admin::read')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getScheduleById(
            @PathVariable("schedule-id") String id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(scheduleService.getScheduleById(id, connectedUser)));
    }

    @PutMapping("/update/status/{schedule-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateScheduleStatus(
            @PathVariable("schedule-id") String id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(scheduleService.updateScheduleStatus(id, connectedUser));
    }

    @PutMapping("/update/{schedule-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> updateSchedule(
            @PathVariable("schedule-id") String id,
            @RequestBody @Valid ScheduleRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(scheduleService.updateSchedule(id, request, connectedUser)));
    }

    @DeleteMapping("/delete/{schedule-id}")
    @PreAuthorize("hasAuthority('admin::delete')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> deleteSchedule(
            @PathVariable("schedule-id") String id, Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(scheduleService.deleteSchedule(id, connectedUser)));
    }
}
