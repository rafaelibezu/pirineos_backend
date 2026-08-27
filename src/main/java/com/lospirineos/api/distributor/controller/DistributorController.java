package com.lospirineos.api.distributor.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.distributor.dto.request.CreateDistributorRequest;
import com.lospirineos.api.distributor.dto.request.UpdateDistributorRequest;
import com.lospirineos.api.distributor.dto.response.DistributorResponse;
import com.lospirineos.api.distributor.service.DistributorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1/distributors")
@RequiredArgsConstructor
@Tag(
        name = "Distribuidores",
        description = "Gestión de distribuidores y proveedores"
)
public class DistributorController {

    private final DistributorService distributorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DistributorResponse>> create(
            @Valid @RequestBody CreateDistributorRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Distribuidor creado correctamente",
                                distributorService.create(request)
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<DistributorResponse>>> findAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        distributorService.findAll()
                )
        );
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')")
    public ResponseEntity<ApiResponse<List<DistributorResponse>>> findAllActive() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        distributorService.findAllActive()
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DistributorResponse>> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        distributorService.findById(id)
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DistributorResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDistributorRequest request
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Distribuidor actualizado correctamente",
                        distributorService.update(
                                id,
                                request
                        )
                )
        );
    }
}