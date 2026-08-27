package com.lospirineos.api.movement.service;

import com.lospirineos.api.movement.dto.request.CreateCashMovementRequest;
import com.lospirineos.api.movement.dto.response.CashMovementResponse;

import java.util.List;

public interface CashMovementService {

    CashMovementResponse create(
            Long authenticatedUserId,
            CreateCashMovementRequest request
    );

    CashMovementResponse findById(
            Long id
    );

    List<CashMovementResponse> findAll();

    List<CashMovementResponse> findMine(
            Long authenticatedUserId
    );
}