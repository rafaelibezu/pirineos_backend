package com.lospirineos.api.collection.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CollectionSummaryResponse(

        LocalDate date,

        BigDecimal totalIncome,

        BigDecimal totalPayment,

        BigDecimal accountableBalance,

        BigDecimal slotMachineBalance,

        List<AccountCollectionResponse> accounts
) {
}