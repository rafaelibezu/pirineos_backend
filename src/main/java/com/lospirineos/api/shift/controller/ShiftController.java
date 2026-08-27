package com.lospirineos.api.shift.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.shift.dto.request.CreateShiftRequest;
import com.lospirineos.api.shift.dto.request.UpdateShiftRequest;
import com.lospirineos.api.shift.dto.response.ShiftResponse;
import com.lospirineos.api.shift.service.ShiftService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shifts")
@RequiredArgsConstructor
@Tag(name = "Turnos", description = "Gestión de turnos")
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShiftResponse>> create(
            @Valid @RequestBody CreateShiftRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Turno creado correctamente",
                                shiftService.create(request)
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        shiftService.findAll()
                )
        );
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> findAllActive() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        shiftService.findAllActive()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShiftResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        shiftService.findById(id)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ShiftResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShiftRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Turno actualizado correctamente",
                        shiftService.update(
                                id,
                                request
                        )
                )
        );
    }
}