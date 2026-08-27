package com.lospirineos.api.closing.service;

import com.lospirineos.api.closing.dto.request.CreateDailyClosingRequest;
import com.lospirineos.api.closing.dto.request.UpdateClosingRealValuesRequest;
import com.lospirineos.api.closing.dto.response.DailyClosingResponse;

import java.util.List;

public interface DailyClosingService {

    DailyClosingResponse create(
            Long authenticatedUserId,
            CreateDailyClosingRequest request
    );

    DailyClosingResponse updateRealValues(
            Long closingId,
            Long authenticatedUserId,
            UpdateClosingRealValuesRequest request
    );

    DailyClosingResponse finalizeClosing(
            Long closingId,
            Long authenticatedUserId
    );

    DailyClosingResponse findById(
            Long closingId
    );

    List<DailyClosingResponse> findAll();
}