package com.lospirineos.api.collection.service;

import com.lospirineos.api.collection.dto.response.CollectionSummaryResponse;

import java.time.LocalDate;

public interface CollectionService {

    CollectionSummaryResponse getDailySummary(
            LocalDate date
    );

    CollectionSummaryResponse getMyDailySummary(
            Long authenticatedUserId,
            LocalDate date
    );
}