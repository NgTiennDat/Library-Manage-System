package com.datien.lms.controller;

import com.datien.lms.dto.request.AuthRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Management")
@RequiredArgsConstructor
public class ManagementController {

    @Operation(
            description = "Get endpoint for manager",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public String get() {
        return "GET:: management controller";
    }
//    @PostMapping
//    public String post() {
//        return "POST:: management controller";
//    }
//    @PutMapping
//    public String put() {
//        return "PUT:: management controller";
//    }
//    @DeleteMapping
//    public String delete() {
//        return "DELETE:: management controller";
//    }

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
    @PostMapping
    public ResponseEntity<?> createdAdmin(
            @RequestBody AuthRequest authRequest
    ) {
        return ResponseEntity.status(201).body("Created successfully");
    }

}