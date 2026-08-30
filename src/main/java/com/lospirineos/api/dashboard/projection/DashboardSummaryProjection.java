package com.lospirineos.api.dashboard.projection;

import java.math.BigDecimal;

public interface DashboardSummaryProjection {

    Long getTotalClosings();

    BigDecimal getTotalSales();

    BigDecimal getTotalManual();

    BigDecimal getTotalCash();

    BigDecimal getTotalDeclaredVisa();

    BigDecimal getTotalRealPos();

    BigDecimal getTotalDistributorPayments();

    BigDecimal getTotalCashDifference();

    BigDecimal getTotalPosDifference();
}