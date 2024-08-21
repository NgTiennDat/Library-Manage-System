package com.datien.lms.controller;

import com.datien.lms.dto.request.AdminRequest;
import com.datien.lms.service.AdminService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/{adminId}")
    @PreAuthorize("hasAuthority('admin::read')")
    @Operation(
            description = "Get an admin by ID",
            summary = "Endpoint to get an admin by ID",
            responses = {
                    @ApiResponse(description = "Admin found", responseCode = "200"),
                    @ApiResponse(description = "Admin not found", responseCode = "404")
            }
    )
    public ResponseEntity<?> getAdminById(@PathVariable Long adminId) {
        var admin = adminService.getAdminById(adminId);
        return ResponseEntity.ok(admin);
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
            @RequestBody AdminRequest request, Authentication connectedUser
    ) {
        adminService.createAdmin(request, connectedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created successfully");
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
        adminService.updateAdminInfo(request, adminId, connectedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Updated successfully");
    }

    @DeleteMapping("/{adminId}")
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
    public ResponseEntity<?> delete(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
