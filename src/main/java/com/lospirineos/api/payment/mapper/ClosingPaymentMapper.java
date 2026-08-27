package com.lospirineos.api.payment.mapper;

import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;
import com.lospirineos.api.payment.entity.ClosingPayment;

public final class ClosingPaymentMapper {

    private ClosingPaymentMapper() {
    }

    public static ClosingPaymentResponse toResponse(
            ClosingPayment payment
    ) {

        return new ClosingPaymentResponse(
                payment.getId(),
                payment.getInvoiceDate(),
                payment.getDistributor().getId(),
                payment.getDistributor().getName(),
                payment.getAmount(),
                payment.getPhotoUrl()
        );
    }
}