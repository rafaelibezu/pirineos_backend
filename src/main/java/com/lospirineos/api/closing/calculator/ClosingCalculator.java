package com.lospirineos.api.closing.calculator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClosingCalculator {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO;

    public static BigDecimal calculateTotalPayments(
            Collection<BigDecimal> payments
    ) {

        if (payments == null || payments.isEmpty()) {
            return ZERO;
        }

        return payments.stream()
                .filter(value -> value != null)
                .reduce(ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateCashTotal(
            BigDecimal coins,
            BigDecimal banknotes
    ) {

        return valueOrZero(coins)
                .add(valueOrZero(banknotes));
    }

    public static BigDecimal calculateExpectedCash(
            BigDecimal zClosing,
            BigDecimal manualAmount,
            BigDecimal declaredVisa,
            BigDecimal totalPayments
    ) {

        return valueOrZero(zClosing)
                .add(valueOrZero(manualAmount))
                .subtract(valueOrZero(declaredVisa))
                .subtract(valueOrZero(totalPayments));
    }

    public static BigDecimal calculateCashDifference(
            BigDecimal cashTotal,
            BigDecimal expectedCash
    ) {

        return valueOrZero(cashTotal)
                .subtract(valueOrZero(expectedCash));
    }

    public static BigDecimal calculatePosDifference(
            BigDecimal realPos,
            BigDecimal declaredVisa
    ) {

        return valueOrZero(realPos)
                .subtract(valueOrZero(declaredVisa));
    }

    private static BigDecimal valueOrZero(
            BigDecimal value
    ) {
        return value == null ? ZERO : value;
    }
}