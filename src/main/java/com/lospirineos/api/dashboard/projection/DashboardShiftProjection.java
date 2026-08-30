package com.lospirineos.api.dashboard.projection;

import java.math.BigDecimal;

public interface DashboardShiftProjection {

    Long getShiftId();

    String getShiftCode();

    String getShiftName();

    Long getTotalClosings();

    BigDecimal getTotalSales();

    BigDecimal getTotalManual();

    BigDecimal getTotalCash();

    BigDecimal getTotalPayments();
}