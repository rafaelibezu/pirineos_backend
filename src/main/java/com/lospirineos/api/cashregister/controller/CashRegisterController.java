package com.lospirineos.api.cashregister.controller;

import com.lospirineos.api.cashregister.dto.request.CreateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.request.UpdateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.response.CashRegisterResponse;
import com.lospirineos.api.cashregister.service.CashRegisterService;
import com.lospirineos.api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cash-registers")
@RequiredArgsConstructor
@Tag(name = "Cajas", description = "Gestión de cajas")
public class CashRegisterController {

    private final CashRegisterService cashRegisterService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CashRegisterResponse>> create(
            @Valid @RequestBody CreateCashRegisterRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Caja creada correctamente",
                                cashRegisterService.create(request)
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CashRegisterResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashRegisterService.findAll()
                )
        );
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<List<CashRegisterResponse>>> findAllActive() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashRegisterService.findAllActive()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CashRegisterResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashRegisterService.findById(id)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CashRegisterResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCashRegisterRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Caja actualizada correctamente",
                        cashRegisterService.update(
                                id,
                                request
                        )
                )
        );
    }
}