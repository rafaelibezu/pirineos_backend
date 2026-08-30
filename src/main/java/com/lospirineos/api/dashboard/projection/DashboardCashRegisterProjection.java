package com.lospirineos.api.dashboard.projection;

import java.math.BigDecimal;

public interface DashboardCashRegisterProjection {

    Long getCashRegisterId();

    String getCashRegisterCode();

    String getCashRegisterName();

    Long getTotalClosings();

    BigDecimal getTotalSales();

    BigDecimal getTotalCash();

    BigDecimal getCashDifference();

    BigDecimal getPosDifference();
}