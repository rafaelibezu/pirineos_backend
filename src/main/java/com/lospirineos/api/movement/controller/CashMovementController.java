package com.lospirineos.api.movement.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.movement.dto.request.CreateCashMovementRequest;
import com.lospirineos.api.movement.dto.response.CashMovementResponse;
import com.lospirineos.api.movement.service.CashMovementService;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movements")
@RequiredArgsConstructor
@Tag(
        name = "Ingresos y pagos",
        description = "Gestión de ingresos y pagos internos"
)
public class CashMovementController {

    private final CashMovementService cashMovementService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<CashMovementResponse>> create(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreateCashMovementRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Movimiento registrado correctamente",
                                cashMovementService.create(
                                        authenticatedUser.getId(),
                                        request
                                )
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CashMovementResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashMovementService.findAll()
                )
        );
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<List<CashMovementResponse>>> findMine(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashMovementService.findMine(
                                authenticatedUser.getId()
                        )
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CashMovementResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashMovementService.findById(id)
                )
        );
    }
}