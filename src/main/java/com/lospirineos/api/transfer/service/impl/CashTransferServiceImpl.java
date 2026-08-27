package com.lospirineos.api.transfer.service.impl;

import com.lospirineos.api.cashregister.entity.CashRegister;
import com.lospirineos.api.cashregister.repository.CashRegisterRepository;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.shift.repository.ShiftRepository;
import com.lospirineos.api.transfer.dto.request.CreateCashTransferRequest;
import com.lospirineos.api.transfer.dto.response.CashTransferResponse;
import com.lospirineos.api.transfer.entity.CashTransfer;
import com.lospirineos.api.transfer.mapper.CashTransferMapper;
import com.lospirineos.api.transfer.repository.CashTransferRepository;
import com.lospirineos.api.transfer.service.CashTransferService;
import com.lospirineos.api.user.entity.User;
import com.lospirineos.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CashTransferServiceImpl
        implements CashTransferService {

    private final CashTransferRepository cashTransferRepository;
    private final ShiftRepository shiftRepository;
    private final CashRegisterRepository cashRegisterRepository;
    private final UserRepository userRepository;

    @Override
    public CashTransferResponse create(
            Long authenticatedUserId,
            CreateCashTransferRequest request
    ) {

        if (
                request.sourceCashRegisterId()
                        .equals(request.destinationCashRegisterId())
        ) {
            throw new BusinessException(
                    "La caja de origen y la caja de destino deben ser diferentes"
            );
        }

        User responsibleUser =
                findUser(authenticatedUserId);

        Shift shift =
                shiftRepository
                        .findById(request.shiftId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Turno no encontrado"
                                )
                        );

        if (!Boolean.TRUE.equals(shift.getActive())) {
            throw new BusinessException(
                    "El turno seleccionado está inactivo"
            );
        }

        CashRegister source =
                findCashRegister(
                        request.sourceCashRegisterId()
                );

        CashRegister destination =
                findCashRegister(
                        request.destinationCashRegisterId()
                );

        validateActiveCashRegister(source);
        validateActiveCashRegister(destination);

        CashTransfer transfer =
                new CashTransfer();

        transfer.setTransferAt(
                OffsetDateTime.now()
        );

        transfer.setShift(shift);
        transfer.setResponsibleUser(responsibleUser);
        transfer.setSourceCashRegister(source);
        transfer.setDestinationCashRegister(destination);
        transfer.setAmount(request.amount());

        transfer.setReason(
                request.reason() != null
                        ? request.reason().trim()
                        : null
        );

        return CashTransferMapper.toResponse(
                cashTransferRepository.save(transfer)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CashTransferResponse findById(
            Long id
    ) {

        CashTransfer transfer =
                cashTransferRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Traspaso no encontrado"
                                )
                        );

        return CashTransferMapper.toResponse(
                transfer
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashTransferResponse> findAll() {

        return cashTransferRepository
                .findAll()
                .stream()
                .map(CashTransferMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashTransferResponse> findMine(
            Long authenticatedUserId
    ) {

        return cashTransferRepository
                .findAllByResponsibleUserId(
                        authenticatedUserId
                )
                .stream()
                .map(CashTransferMapper::toResponse)
                .toList();
    }

    private User findUser(
            Long userId
    ) {

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }

    private CashRegister findCashRegister(
            Long id
    ) {

        return cashRegisterRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Caja no encontrada"
                        )
                );
    }

    private void validateActiveCashRegister(
            CashRegister cashRegister
    ) {

        if (!Boolean.TRUE.equals(cashRegister.getActive())) {
            throw new BusinessException(
                    "La caja seleccionada se encuentra inactiva"
            );
        }
    }
}