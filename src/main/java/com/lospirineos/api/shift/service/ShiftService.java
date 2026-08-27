package com.lospirineos.api.shift.service;

import com.lospirineos.api.shift.dto.request.CreateShiftRequest;
import com.lospirineos.api.shift.dto.request.UpdateShiftRequest;
import com.lospirineos.api.shift.dto.response.ShiftResponse;

import java.util.List;

public interface ShiftService {

    ShiftResponse create(
            CreateShiftRequest request
    );

    ShiftResponse findById(
            Long id
    );

    List<ShiftResponse> findAll();

    List<ShiftResponse> findAllActive();

    ShiftResponse update(
            Long id,
            UpdateShiftRequest request
    );
}