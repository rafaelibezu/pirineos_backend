package com.lospirineos.api.collection.service.impl;

import com.lospirineos.api.collection.dto.response.AccountCollectionResponse;
import com.lospirineos.api.collection.dto.response.CollectionSummaryResponse;
import com.lospirineos.api.collection.service.CollectionService;
import com.lospirineos.api.common.enums.AccountType;
import com.lospirineos.api.common.enums.MovementType;
import com.lospirineos.api.movement.entity.CashMovement;
import com.lospirineos.api.movement.repository.CashMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollectionServiceImpl
        implements CollectionService {

    private static final BigDecimal ZERO =
            BigDecimal.ZERO;

    private static final ZoneId BUSINESS_ZONE =
            ZoneId.of("Europe/Madrid");

    private final CashMovementRepository cashMovementRepository;

    @Override
    public CollectionSummaryResponse getDailySummary(
            LocalDate date
    ) {

        DateRange range =
                buildDateRange(date);

        List<CashMovement> movements =
                cashMovementRepository
                        .findAllByMovementAtBetween(
                                range.start(),
                                range.end()
                        );

        return buildSummary(
                date,
                movements
        );
    }

    @Override
    public CollectionSummaryResponse getMyDailySummary(
            Long authenticatedUserId,
            LocalDate date
    ) {

        DateRange range =
                buildDateRange(date);

        List<CashMovement> movements =
                cashMovementRepository
                        .findAllByUserIdAndMovementAtBetween(
                                authenticatedUserId,
                                range.start(),
                                range.end()
                        );

        return buildSummary(
                date,
                movements
        );
    }

    private CollectionSummaryResponse buildSummary(
            LocalDate date,
            List<CashMovement> movements
    ) {

        List<AccountCollectionResponse> accountSummaries =
                Arrays.stream(AccountType.values())
                        .map(account ->
                                buildAccountSummary(
                                        account,
                                        movements
                                )
                        )
                        .toList();

        BigDecimal totalIncome =
                sumByMovementType(
                        movements,
                        MovementType.INCOME
                );

        BigDecimal totalPayment =
                sumByMovementType(
                        movements,
                        MovementType.PAYMENT
                );

        BigDecimal accountableBalance =
                accountSummaries
                        .stream()
                        .filter(summary ->
                                !AccountType.SLOT_MACHINE
                                        .equals(summary.account())
                        )
                        .map(AccountCollectionResponse::balance)
                        .reduce(
                                ZERO,
                                BigDecimal::add
                        );

        BigDecimal slotMachineBalance =
                accountSummaries
                        .stream()
                        .filter(summary ->
                                AccountType.SLOT_MACHINE
                                        .equals(summary.account())
                        )
                        .map(AccountCollectionResponse::balance)
                        .findFirst()
                        .orElse(ZERO);

        return new CollectionSummaryResponse(
                date,
                totalIncome,
                totalPayment,
                accountableBalance,
                slotMachineBalance,
                accountSummaries
        );
    }

    private AccountCollectionResponse buildAccountSummary(
            AccountType account,
            List<CashMovement> movements
    ) {

        BigDecimal income =
                movements
                        .stream()
                        .filter(movement ->
                                account.equals(
                                        movement.getAccount()
                                )
                        )
                        .filter(movement ->
                                MovementType.INCOME.equals(
                                        movement.getMovementType()
                                )
                        )
                        .map(CashMovement::getAmount)
                        .reduce(
                                ZERO,
                                BigDecimal::add
                        );

        BigDecimal payment =
                movements
                        .stream()
                        .filter(movement ->
                                account.equals(
                                        movement.getAccount()
                                )
                        )
                        .filter(movement ->
                                MovementType.PAYMENT.equals(
                                        movement.getMovementType()
                                )
                        )
                        .map(CashMovement::getAmount)
                        .reduce(
                                ZERO,
                                BigDecimal::add
                        );

        return new AccountCollectionResponse(
                account,
                account.getDisplayName(),
                income,
                payment,
                income.subtract(payment)
        );
    }

    private BigDecimal sumByMovementType(
            List<CashMovement> movements,
            MovementType type
    ) {

        return movements
                .stream()
                .filter(movement ->
                        type.equals(
                                movement.getMovementType()
                        )
                )
                .map(CashMovement::getAmount)
                .reduce(
                        ZERO,
                        BigDecimal::add
                );
    }

    private DateRange buildDateRange(
            LocalDate date
    ) {

        OffsetDateTime start =
                date
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        OffsetDateTime end =
                date
                        .plusDays(1)
                        .atStartOfDay(BUSINESS_ZONE)
                        .toOffsetDateTime();

        return new DateRange(
                start,
                end
        );
    }

    private record DateRange(
            OffsetDateTime start,
            OffsetDateTime end
    ) {
    }
}