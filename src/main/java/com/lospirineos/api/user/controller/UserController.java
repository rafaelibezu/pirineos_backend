package com.lospirineos.api.user.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.user.dto.request.CreateUserRequest;
import com.lospirineos.api.user.dto.request.UpdateUserRequest;
import com.lospirineos.api.user.dto.response.UserResponse;
import com.lospirineos.api.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Usuarios", description = "Administración de usuarios")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse response =
                userService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Usuario creado correctamente",
                                response
                        )
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.findById(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.findAll()
                )
        );
    }

    @GetMapping("/waiters")
    public ResponseEntity<ApiResponse<List<UserResponse>>> findActiveWaiters() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        userService.findActiveWaiters()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Usuario actualizado correctamente",
                        userService.update(
                                id,
                                request
                        )
                )
        );
    }
}