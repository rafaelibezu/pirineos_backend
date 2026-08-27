package com.lospirineos.api.payment.controller;

import com.lospirineos.api.common.response.ApiResponse;
import com.lospirineos.api.payment.dto.request.CreateClosingPaymentRequest;
import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;
import com.lospirineos.api.payment.service.ClosingPaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/closings/{closingId}/payments")
@RequiredArgsConstructor
@Tag(name = "Pagos de cierre", description = "Pagos asociados a cierres diarios")
public class ClosingPaymentController {

    private final ClosingPaymentService closingPaymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<ClosingPaymentResponse>> create(
            @PathVariable Long closingId,
            @RequestHeader("X-User-Id") Long authenticatedUserId,
            @Valid @RequestBody CreateClosingPaymentRequest request
    ) {

        ClosingPaymentResponse response =
                closingPaymentService.create(
                        closingId,
                        authenticatedUserId,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Pago agregado correctamente",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClosingPaymentResponse>>> findAll(
            @PathVariable Long closingId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        closingPaymentService
                                .findAllByClosing(closingId)
                )
        );
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long closingId,
            @PathVariable Long paymentId
    ) {

        closingPaymentService.delete(
                closingId,
                paymentId
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Pago eliminado correctamente",
                        null
                )
        );
    }
}