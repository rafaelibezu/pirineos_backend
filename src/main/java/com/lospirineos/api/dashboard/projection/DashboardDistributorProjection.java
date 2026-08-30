package com.lospirineos.api.dashboard.projection;

import java.math.BigDecimal;

public interface DashboardDistributorProjection {

    Long getDistributorId();

    String getDistributorName();

    Long getTotalInvoices();

    BigDecimal getTotalPaid();
}