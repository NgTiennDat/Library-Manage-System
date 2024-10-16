package com.datien.lms.controller;

import com.datien.lms.dto.request.model.UserRequest;
import com.datien.lms.dto.response.baseResponse.ResponseData;
import com.datien.lms.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('manager::create')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createAdmin(
            @RequestBody UserRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(managerService.createAdmin(request, connectedUser)));
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
    @PreAuthorize("hasAuthority('manager::update')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updateAdmin(
            @RequestBody UserRequest request,
            @PathVariable("admin-id") String adminId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(managerService.updateAdminInfo(request, adminId, connectedUser)));
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
    @PreAuthorize("hasAuthority('manager::delete')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> deleteAdmin(
            @PathVariable("admin-id") String adminId,
            @RequestParam(name = "isDeleted", defaultValue = "") String isDeleted,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(managerService.deleteAdmin(adminId, isDeleted, connectedUser)));
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
    @PreAuthorize("hasAnyAuthority('manager::read')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAllAdmins(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(ResponseData.createResponse(managerService.getAllAdmins(page, size, connectedUser)));
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
    @PreAuthorize("hasAuthority('manager::read')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getAdminDetail(
            @PathVariable("admin-id") String adminId,
            Authentication connectedUser
    ) {
       return ResponseEntity.ok(ResponseData.createResponse(managerService.getAdminDetail(adminId, connectedUser)));
    }
}
