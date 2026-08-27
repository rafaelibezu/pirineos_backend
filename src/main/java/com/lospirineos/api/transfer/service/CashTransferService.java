package com.lospirineos.api.transfer.service;

import com.lospirineos.api.transfer.dto.request.CreateCashTransferRequest;
import com.lospirineos.api.transfer.dto.response.CashTransferResponse;

import java.util.List;

public interface CashTransferService {

    CashTransferResponse create(
            Long authenticatedUserId,
            CreateCashTransferRequest request
    );

    CashTransferResponse findById(
            Long id
    );

    List<CashTransferResponse> findAll();

    List<CashTransferResponse> findMine(
            Long authenticatedUserId
    );
}