package com.lospirineos.api.payment.service;

import com.lospirineos.api.payment.dto.request.CreateClosingPaymentRequest;
import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;

import java.util.List;

public interface ClosingPaymentService {

    ClosingPaymentResponse create(
            Long closingId,
            Long authenticatedUserId,
            CreateClosingPaymentRequest request
    );

    List<ClosingPaymentResponse> findAllByClosing(
            Long closingId
    );

    void delete(
            Long closingId,
            Long paymentId
    );
}