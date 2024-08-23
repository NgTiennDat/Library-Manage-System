package com.datien.lms.controller;

import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.dto.response.AdminResponse;
import com.datien.lms.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Management")
@RequiredArgsConstructor
public class ManagementController {

    private final ManagerService managerService;

    @Operation(
            description = "Create a new admin",
            summary = "Endpoint to create a new admin",
            responses = {
                    @ApiResponse(
                            description = "Admin created successfully",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAdmin(
            @RequestBody AdminRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(managerService.createAdmin(request, connectedUser));
    }

    @Operation(
            description = "Update an existing admin",
            summary = "Endpoint to update an existing admin",
            responses = {
                    @ApiResponse(
                            description = "Admin updated successfully",
                            responseCode = "202"
                    ),
                    @ApiResponse(
                            description = "Admin not found",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/update/{admin-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateAdmin(
            @RequestBody AdminRequest request,
            @PathVariable("admin-id") Long adminId,
            Authentication connectedUser
    ) throws IllegalAccessException {
        return ResponseEntity.ok(managerService.updateAdminInfo(request, adminId, connectedUser));
    }

    @Operation(
            description = "Delete an existing admin",
            summary = "Endpoint to delete an existing admin",
            responses = {
                    @ApiResponse(
                            description = "Admin deleted successfully",
                            responseCode = "202"
                    ),
                    @ApiResponse(
                            description = "Admin not found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/delete/{admin-id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> deleteAdmin(
            @PathVariable("admin-id") Long adminId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(managerService.deleteAdmin(adminId, connectedUser));
    }

    @Operation(
            description = "Get all admins",
            summary = "Endpoint to get all admins",
            responses = {
                    @ApiResponse(
                            description = "Admins retrieved successfully",
                            responseCode = "200"
                    )
            }
    )
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(managerService.getAllAdmins(page, size, connectedUser));
    }

    @Operation(
            description = "Get admin details",
            summary = "Endpoint to get admin details",
            responses = {
                    @ApiResponse(
                            description = "Admin retrieved successfully",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Admin not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/detail/{admin-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAdminDetail(
            @PathVariable("admin-id") Long adminId,
            Authentication connectedUser
    ) {
       return ResponseEntity.ok(managerService.getAdminDetail(adminId, connectedUser));
    }
}
