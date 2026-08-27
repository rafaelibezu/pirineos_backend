package com.lospirineos.api.cashregister.service;

import com.lospirineos.api.cashregister.dto.request.CreateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.request.UpdateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.response.CashRegisterResponse;

import java.util.List;

public interface CashRegisterService {

    CashRegisterResponse create(
            CreateCashRegisterRequest request
    );

    CashRegisterResponse findById(
            Long id
    );

    List<CashRegisterResponse> findAll();

    List<CashRegisterResponse> findAllActive();

    CashRegisterResponse update(
            Long id,
            UpdateCashRegisterRequest request
    );
}