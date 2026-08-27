package com.lospirineos.api.closing.service.impl;

import com.lospirineos.api.cashregister.entity.CashRegister;
import com.lospirineos.api.cashregister.repository.CashRegisterRepository;
import com.lospirineos.api.closing.calculator.ClosingCalculator;
import com.lospirineos.api.closing.dto.request.CreateDailyClosingRequest;
import com.lospirineos.api.closing.dto.request.UpdateClosingRealValuesRequest;
import com.lospirineos.api.closing.dto.response.DailyClosingResponse;
import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.closing.mapper.DailyClosingMapper;
import com.lospirineos.api.closing.repository.DailyClosingRepository;
import com.lospirineos.api.closing.service.DailyClosingService;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.common.exception.*;
import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;
import com.lospirineos.api.payment.entity.ClosingPayment;
import com.lospirineos.api.payment.mapper.ClosingPaymentMapper;
import com.lospirineos.api.payment.repository.ClosingPaymentRepository;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.shift.repository.ShiftRepository;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lospirineos.api.common.constant.RoleCode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyClosingServiceImpl implements DailyClosingService {

    private static final BigDecimal ZERO = BigDecimal.ZERO;

    private final DailyClosingRepository dailyClosingRepository;
    private final ClosingPaymentRepository closingPaymentRepository;
    private final ShiftRepository shiftRepository;
    private final CashRegisterRepository cashRegisterRepository;
    private final UserRepository userRepository;

    @Override
    public DailyClosingResponse create(
            Long authenticatedUserId,
            CreateDailyClosingRequest request
    ) {

        User authenticatedUser = findUser(authenticatedUserId);
        User waiter = findUser(request.waiterId());

        validateWaiterOwnership(
                authenticatedUser,
                waiter
        );

        Shift shift = shiftRepository.findById(request.shiftId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("El turno no existe")
                );

        CashRegister cashRegister =
                cashRegisterRepository.findById(request.cashRegisterId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "La caja no existe"
                                )
                        );

        if (!waiter.getActive()) {
            throw new BusinessException(
                    "El camarero se encuentra inactivo"
            );
        }

        if (!shift.getActive()) {
            throw new BusinessException(
                    "El turno se encuentra inactivo"
            );
        }

        if (!cashRegister.getActive()) {
            throw new BusinessException(
                    "La caja se encuentra inactiva"
            );
        }

        DailyClosing closing = DailyClosing.builder()
                .closingAt(OffsetDateTime.now())
                .shift(shift)
                .cashRegister(cashRegister)
                .waiter(waiter)

                // PASO 1
                .zClosing(request.zClosing())
                .manualAmount(request.manualAmount())
                .declaredVisa(request.declaredVisa())
                .closingPhotoUrl(request.closingPhotoUrl())

                // Inicialmente todavía no hay pagos
                .totalPayments(ZERO)

                // PASO 2 todavía no completado
                .coins(ZERO)
                .banknotes(ZERO)
                .realPos(ZERO)

                .cashTotal(ZERO)
                .expectedCash(ZERO)
                .cashDifference(ZERO)
                .posDifference(ZERO)

                .status(ClosingStatus.DRAFT)

                .createdBy(authenticatedUser)
                .build();

        DailyClosing saved = dailyClosingRepository.save(closing);

        return buildResponse(saved);
    }

    private void validateWaiterOwnership(
            User authenticatedUser,
            User waiter
    ) {

        if (!RoleCode.WAITER.equals(waiter.getRole().getCode())) {
            throw new BusinessException(
                    "El usuario seleccionado no es un camarero"
            );
        }

        String authenticatedRole =
                authenticatedUser
                        .getRole()
                        .getCode();

        if (
                RoleCode.WAITER.equals(authenticatedRole)
                        && !authenticatedUser.getId().equals(waiter.getId())
        ) {
            throw new BusinessException(
                    "Un camarero solo puede registrar cierres a su propio nombre"
            );
        }
    }

    @Override
    public DailyClosingResponse updateRealValues(
            Long closingId,
            Long authenticatedUserId,
            UpdateClosingRealValuesRequest request
    ) {

        DailyClosing closing = findClosing(closingId);

        validateDraft(closing);

        User user = findUser(authenticatedUserId);

        BigDecimal totalPayments =
                calculateTotalPayments(closingId);

        BigDecimal cashTotal =
                ClosingCalculator.calculateCashTotal(
                        request.coins(),
                        request.banknotes()
                );

        /*
         * IMPORTANTE:
         *
         * Según la aclaración final del cliente:
         *
         * EFECTIVO ESPERADO =
         * CIERRE Z + MANUAL - VISAS DECLARADAS - PAGOS
         *
         * Aquí NO debemos restar el TPV real.
         */
        BigDecimal expectedCash =
                ClosingCalculator.calculateExpectedCash(
                        closing.getZClosing(),
                        closing.getManualAmount(),
                        closing.getDeclaredVisa(),
                        totalPayments
                );

        BigDecimal cashDifference =
                ClosingCalculator.calculateCashDifference(
                        cashTotal,
                        expectedCash
                );

        BigDecimal posDifference =
                ClosingCalculator.calculatePosDifference(
                        request.realPos(),
                        closing.getDeclaredVisa()
                );

        closing.setTotalPayments(totalPayments);

        closing.setCoins(request.coins());
        closing.setBanknotes(request.banknotes());
        closing.setRealPos(request.realPos());

        closing.setCashTotal(cashTotal);
        closing.setExpectedCash(expectedCash);

        closing.setCashDifference(cashDifference);
        closing.setPosDifference(posDifference);

        closing.setCashierPhotoUrl(request.cashierPhotoUrl());

        closing.setUpdatedBy(user);

        DailyClosing saved =
                dailyClosingRepository.save(closing);

        return buildResponse(saved);
    }

    @Override
    public DailyClosingResponse finalizeClosing(
            Long closingId,
            Long authenticatedUserId
    ) {

        DailyClosing closing = findClosing(closingId);

        validateDraft(closing);

        User user = findUser(authenticatedUserId);

        BigDecimal totalPayments =
                calculateTotalPayments(closingId);

        BigDecimal cashTotal =
                ClosingCalculator.calculateCashTotal(
                        closing.getCoins(),
                        closing.getBanknotes()
                );

        BigDecimal expectedCash =
                ClosingCalculator.calculateExpectedCash(
                        closing.getZClosing(),
                        closing.getManualAmount(),
                        closing.getDeclaredVisa(),
                        totalPayments
                );

        BigDecimal cashDifference =
                ClosingCalculator.calculateCashDifference(
                        cashTotal,
                        expectedCash
                );

        BigDecimal posDifference =
                ClosingCalculator.calculatePosDifference(
                        closing.getRealPos(),
                        closing.getDeclaredVisa()
                );

        closing.setTotalPayments(totalPayments);
        closing.setCashTotal(cashTotal);
        closing.setExpectedCash(expectedCash);
        closing.setCashDifference(cashDifference);
        closing.setPosDifference(posDifference);

        closing.setStatus(ClosingStatus.FINALIZED);
        closing.setUpdatedBy(user);

        DailyClosing saved =
                dailyClosingRepository.save(closing);

        return buildResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DailyClosingResponse findById(Long closingId) {

        DailyClosing closing = findClosing(closingId);

        return buildResponse(closing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyClosingResponse> findAll() {

        return dailyClosingRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    private BigDecimal calculateTotalPayments(
            Long closingId
    ) {

        List<BigDecimal> amounts =
                closingPaymentRepository
                        .findAllByDailyClosingId(closingId)
                        .stream()
                        .map(ClosingPayment::getAmount)
                        .toList();

        return ClosingCalculator
                .calculateTotalPayments(amounts);
    }

    private DailyClosingResponse buildResponse(
            DailyClosing closing
    ) {

        List<ClosingPaymentResponse> payments =
                closingPaymentRepository
                        .findAllByDailyClosingId(closing.getId())
                        .stream()
                        .map(ClosingPaymentMapper::toResponse)
                        .toList();

        return DailyClosingMapper.toResponse(
                closing,
                payments
        );
    }

    private DailyClosing findClosing(Long id) {
        return dailyClosingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El cierre no existe"
                        )
                );
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "El usuario no existe"
                        )
                );
    }

    private void validateDraft(
            DailyClosing closing
    ) {

        if (closing.getStatus() != ClosingStatus.DRAFT) {
            throw new BusinessException(
                    "El cierre ya se encuentra finalizado"
            );
        }
    }
}