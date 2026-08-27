package com.lospirineos.api.cashregister.service.impl;

import com.lospirineos.api.cashregister.dto.request.CreateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.request.UpdateCashRegisterRequest;
import com.lospirineos.api.cashregister.dto.response.CashRegisterResponse;
import com.lospirineos.api.cashregister.entity.CashRegister;
import com.lospirineos.api.cashregister.mapper.CashRegisterMapper;
import com.lospirineos.api.cashregister.repository.CashRegisterRepository;
import com.lospirineos.api.cashregister.service.CashRegisterService;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CashRegisterServiceImpl
        implements CashRegisterService {

    private final CashRegisterRepository cashRegisterRepository;

    @Override
    public CashRegisterResponse create(
            CreateCashRegisterRequest request
    ) {

        String code =
                request.code()
                        .trim()
                        .toUpperCase();

        if (
                cashRegisterRepository
                        .existsByCode(code)
        ) {
            throw new BusinessException(
                    "Ya existe una caja con ese código"
            );
        }

        CashRegister cashRegister =
                new CashRegister();

        cashRegister.setCode(code);
        cashRegister.setName(
                request.name().trim()
        );
        cashRegister.setDescription(
                request.description()
        );
        cashRegister.setActive(true);

        return CashRegisterMapper.toResponse(
                cashRegisterRepository.save(
                        cashRegister
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CashRegisterResponse findById(
            Long id
    ) {

        return CashRegisterMapper.toResponse(
                findCashRegister(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashRegisterResponse> findAll() {

        return cashRegisterRepository
                .findAll()
                .stream()
                .map(CashRegisterMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashRegisterResponse> findAllActive() {

        return cashRegisterRepository
                .findAllByActiveTrue()
                .stream()
                .map(CashRegisterMapper::toResponse)
                .toList();
    }

    @Override
    public CashRegisterResponse update(
            Long id,
            UpdateCashRegisterRequest request
    ) {

        CashRegister cashRegister =
                findCashRegister(id);

        cashRegister.setName(
                request.name().trim()
        );

        cashRegister.setDescription(
                request.description()
        );

        if (request.active() != null) {
            cashRegister.setActive(
                    request.active()
            );
        }

        return CashRegisterMapper.toResponse(
                cashRegisterRepository.save(
                        cashRegister
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
}