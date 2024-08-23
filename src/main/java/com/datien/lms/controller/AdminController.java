package com.datien.lms.controller;

import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.request.StudentRequest;
import com.datien.lms.dto.response.StudentResponse;
import com.datien.lms.service.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{student-id}")
    @PreAuthorize("hasAuthority('admin::read')")
    @Operation(
            description = "Get an admin by ID",
            summary = "Endpoint to get an admin by ID",
            responses = {
                    @ApiResponse(description = "Admin found", responseCode = "200"),
                    @ApiResponse(description = "Admin not found", responseCode = "404")
            }
    )
    public ResponseEntity<?> getStudentId(
            @PathVariable("student-id") Long studentId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(adminService.getStudentById(studentId, connectedUser));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin::create')")
    @Hidden
    @Operation(
            description = "Create a new admin",
            summary = "Endpoint to create a new admin",
            responses = {
                    @ApiResponse(description = "Admin created successfully", responseCode = "201"),
                    @ApiResponse(description = "Invalid request", responseCode = "400")
            }
    )
    public ResponseEntity<?> create(
            @RequestBody StudentRequest request, Authentication connectedUser
    ) {
        return ResponseEntity.ok(adminService.createUser(request, connectedUser));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin::read')")
    @Operation(
            description = "Get an admin by ID",
            summary = "Endpoint to get an admin by ID",
            responses = {
                    @ApiResponse(description = "Admin found", responseCode = "200"),
                    @ApiResponse(description = "Admin not found", responseCode = "404")
            }
    )
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(adminService.getAllStudent(page, size, connectedUser));
    }

    @PutMapping("/{adminId}")
    @PreAuthorize("hasAuthority('admin:update')")
    @Hidden
    @Operation(
            description = "Update an existing admin",
            summary = "Endpoint to update an existing admin",
            responses = {
                    @ApiResponse(description = "Admin updated successfully", responseCode = "202"),
                    @ApiResponse(description = "Admin not found", responseCode = "404")
            }
    )
    public ResponseEntity<?> update(
            @RequestBody AdminRequest request,
            @PathVariable Long adminId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(adminService.updateAdminInfo(request, adminId, connectedUser));
    }

    @DeleteMapping("/{student-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    @Hidden
    @Operation(
            description = "Delete an admin by ID",
            summary = "Endpoint to delete an admin by ID",
            responses = {
                    @ApiResponse(description = "Admin deleted successfully", responseCode = "204"),
                    @ApiResponse(description = "Admin not found", responseCode = "404")
            }
    )
    public ResponseEntity<?> delete(
            @PathVariable("student-id") Long studentId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(adminService.deleteStudent(studentId, connectedUser));
    }

}
