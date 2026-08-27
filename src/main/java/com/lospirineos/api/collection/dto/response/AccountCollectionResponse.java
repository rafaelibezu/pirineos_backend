package com.lospirineos.api.collection.dto.response;

import com.lospirineos.api.common.enums.AccountType;

import java.math.BigDecimal;

public record AccountCollectionResponse(

        AccountType account,
        String accountName,

        BigDecimal totalIncome,
        BigDecimal totalPayment,
        BigDecimal balance
) {
}