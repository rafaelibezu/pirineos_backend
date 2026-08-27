package com.lospirineos.api.closing.mapper;

import com.lospirineos.api.closing.dto.response.DailyClosingResponse;
import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;

import java.util.List;

public final class DailyClosingMapper {

    private DailyClosingMapper() {
    }

    public static DailyClosingResponse toResponse(
            DailyClosing closing,
            List<ClosingPaymentResponse> payments
    ) {

        String waiterName =
                closing.getWaiter().getFirstName()
                        + (closing.getWaiter().getLastName() != null
                        ? " " + closing.getWaiter().getLastName()
                        : "");

        return new DailyClosingResponse(
                closing.getId(),
                closing.getClosingAt(),

                closing.getShift().getId(),
                closing.getShift().getName(),

                closing.getCashRegister().getId(),
                closing.getCashRegister().getName(),

                closing.getWaiter().getId(),
                waiterName,

                closing.getZClosing(),
                closing.getManualAmount(),
                closing.getDeclaredVisa(),

                closing.getTotalPayments(),

                closing.getClosingPhotoUrl(),

                closing.getCoins(),
                closing.getBanknotes(),
                closing.getRealPos(),

                closing.getCashTotal(),
                closing.getExpectedCash(),

                closing.getCashDifference(),
                closing.getPosDifference(),

                closing.getCashierPhotoUrl(),

                closing.getStatus().name(),

                payments
        );
    }
}