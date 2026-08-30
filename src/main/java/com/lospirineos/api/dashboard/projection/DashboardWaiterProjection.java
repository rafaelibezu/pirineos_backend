package com.lospirineos.api.dashboard.projection;

import java.math.BigDecimal;

public interface DashboardWaiterProjection {

    Long getWaiterId();

    String getFirstName();

    String getLastName();

    Long getTotalClosings();

    BigDecimal getTotalSales();

    BigDecimal getTotalManual();

    BigDecimal getCashDifference();

    BigDecimal getPosDifference();
}