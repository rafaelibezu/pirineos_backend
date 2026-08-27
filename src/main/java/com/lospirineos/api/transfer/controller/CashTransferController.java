package com.lospirineos.api.transfer.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import com.lospirineos.api.transfer.dto.request.CreateCashTransferRequest;
import com.lospirineos.api.transfer.dto.response.CashTransferResponse;
import com.lospirineos.api.transfer.service.CashTransferService;
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
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(
        name = "Traspasos de caja",
        description = "Gestión de traspasos entre cajas"
)
public class CashTransferController {

    private final CashTransferService cashTransferService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<CashTransferResponse>> create(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreateCashTransferRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Traspaso registrado correctamente",
                                cashTransferService.create(
                                        authenticatedUser.getId(),
                                        request
                                )
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CashTransferResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashTransferService.findAll()
                )
        );
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<List<CashTransferResponse>>> findMine(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashTransferService.findMine(
                                authenticatedUser.getId()
                        )
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CashTransferResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        cashTransferService.findById(id)
                )
        );
    }
}