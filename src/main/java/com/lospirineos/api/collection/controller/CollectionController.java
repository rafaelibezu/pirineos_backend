package com.lospirineos.api.collection.controller;

import com.lospirineos.api.collection.dto.response.CollectionSummaryResponse;
import com.lospirineos.api.collection.service.CollectionService;
import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.security.principal.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/collection")
@RequiredArgsConstructor
@Tag(
        name = "Recaudación",
        description = "Resumen de ingresos, pagos y saldos"
)
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping("/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CollectionSummaryResponse>> getDailySummary(
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        collectionService.getDailySummary(
                                date
                        )
                )
        );
    }

    @GetMapping("/mine/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<CollectionSummaryResponse>> getMyDailySummary(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        collectionService.getMyDailySummary(
                                authenticatedUser.getId(),
                                date
                        )
                )
        );
    }
}