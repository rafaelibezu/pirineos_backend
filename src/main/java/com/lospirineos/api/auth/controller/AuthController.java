package com.lospirineos.api.auth.controller;

import com.lospirineos.api.auth.dto.request.LoginRequest;
import com.lospirineos.api.auth.dto.response.LoginResponse;
import com.lospirineos.api.auth.service.AuthService;
import com.lospirineos.api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Inicio de sesión y seguridad")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Inicio de sesión correcto",
                        authService.login(request)
                )
        );
    }
}