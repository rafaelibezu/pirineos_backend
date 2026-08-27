package com.lospirineos.api.closing.controller;

import com.lospirineos.api.closing.dto.request.CreateDailyClosingRequest;
import com.lospirineos.api.closing.dto.request.UpdateClosingRealValuesRequest;
import com.lospirineos.api.closing.dto.response.DailyClosingResponse;
import com.lospirineos.api.closing.service.DailyClosingService;
import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/closings")
@RequiredArgsConstructor
@Tag(name = "Cierres", description = "Gestión de cierres diarios")
public class DailyClosingController {

    private final DailyClosingService dailyClosingService;

    @PostMapping
    public ResponseEntity<ApiResponse<DailyClosingResponse>> create(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody CreateDailyClosingRequest request
    ) {

        DailyClosingResponse response =
                dailyClosingService.create(
                        authenticatedUser.getId(),
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Cierre creado correctamente",
                                response
                        )
                );
    }

    @PutMapping("/{closingId}/real-values")
    public ResponseEntity<ApiResponse<DailyClosingResponse>> updateRealValues(
            @PathVariable Long closingId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @Valid @RequestBody UpdateClosingRealValuesRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Valores reales actualizados correctamente",
                        dailyClosingService.updateRealValues(
                                closingId,
                                authenticatedUser.getId(),
                                request
                        )
                )
        );
    }

    @PostMapping("/{closingId}/finalize")
    public ResponseEntity<ApiResponse<DailyClosingResponse>> finalizeClosing(
            @PathVariable Long closingId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cierre finalizado correctamente",
                        dailyClosingService.finalizeClosing(
                                closingId,
                                authenticatedUser.getId()
                        )
                )
        );
    }

    @GetMapping("/{closingId}")
    public ResponseEntity<ApiResponse<DailyClosingResponse>> findById(
            @PathVariable Long closingId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        dailyClosingService.findById(
                                closingId
                        )
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DailyClosingResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        dailyClosingService.findAll()
                )
        );
    }
}