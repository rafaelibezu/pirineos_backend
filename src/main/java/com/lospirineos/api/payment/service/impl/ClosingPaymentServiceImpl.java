package com.lospirineos.api.payment.service.impl;

import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.closing.repository.DailyClosingRepository;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.distributor.entity.Distributor;
import com.lospirineos.api.distributor.repository.DistributorRepository;
import com.lospirineos.api.payment.dto.request.CreateClosingPaymentRequest;
import com.lospirineos.api.payment.dto.response.ClosingPaymentResponse;
import com.lospirineos.api.payment.entity.ClosingPayment;
import com.lospirineos.api.payment.mapper.ClosingPaymentMapper;
import com.lospirineos.api.payment.repository.ClosingPaymentRepository;
import com.lospirineos.api.payment.service.ClosingPaymentService;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClosingPaymentServiceImpl implements ClosingPaymentService {

    private final ClosingPaymentRepository closingPaymentRepository;
    private final DailyClosingRepository dailyClosingRepository;
    private final DistributorRepository distributorRepository;
    private final UserRepository userRepository;

    @Override
    public ClosingPaymentResponse create(
            Long closingId,
            Long authenticatedUserId,
            CreateClosingPaymentRequest request
    ) {

        DailyClosing closing = findClosing(closingId);

        validateDraft(closing);

        Distributor distributor = distributorRepository.findById(request.distributorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("El distribuidor no existe")
                );

        if (!distributor.getActive()) {
            throw new BusinessException("El distribuidor se encuentra inactivo");
        }

        User user = findUser(authenticatedUserId);

        ClosingPayment payment = ClosingPayment.builder()
                .dailyClosing(closing)
                .distributor(distributor)
                .invoiceDate(request.invoiceDate())
                .amount(request.amount())
                .photoUrl(request.photoUrl())
                .createdBy(user)
                .build();

        ClosingPayment saved = closingPaymentRepository.save(payment);
        recalculateClosingTotalPayments(closing);
        return ClosingPaymentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClosingPaymentResponse> findAllByClosing(Long closingId) {

        if (!dailyClosingRepository.existsById(closingId)) {
            throw new ResourceNotFoundException("El cierre no existe");
        }

        return closingPaymentRepository
                .findAllByDailyClosingId(closingId)
                .stream()
                .map(ClosingPaymentMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(
            Long closingId,
            Long paymentId
    ) {

        DailyClosing closing = findClosing(closingId);

        validateDraft(closing);

        ClosingPayment payment = closingPaymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("El pago no existe")
                );

        if (!payment.getDailyClosing().getId().equals(closingId)) {
            throw new BusinessException(
                    "El pago no pertenece al cierre indicado"
            );
        }

        closingPaymentRepository.delete(payment);
        closingPaymentRepository.flush();
        recalculateClosingTotalPayments(closing);
    }

    private DailyClosing findClosing(Long id) {
        return dailyClosingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("El cierre no existe")
                );
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("El usuario no existe")
                );
    }

    private void validateDraft(DailyClosing closing) {
        if (closing.getStatus() != ClosingStatus.DRAFT) {
            throw new BusinessException(
                    "No se puede modificar un cierre finalizado"
            );
        }
    }

    private void recalculateClosingTotalPayments(
            DailyClosing closing
    ) {

        var amounts = closingPaymentRepository
                .findAllByDailyClosingId(closing.getId())
                .stream()
                .map(ClosingPayment::getAmount)
                .toList();

        var total = amounts.stream()
                .reduce(
                        java.math.BigDecimal.ZERO,
                        java.math.BigDecimal::add
                );

        closing.setTotalPayments(total);

        dailyClosingRepository.save(closing);
    }
}