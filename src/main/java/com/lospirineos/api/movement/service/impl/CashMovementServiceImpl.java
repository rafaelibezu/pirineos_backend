package com.lospirineos.api.movement.service.impl;

import com.lospirineos.api.common.enums.MovementType;
import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.movement.dto.request.CreateCashMovementRequest;
import com.lospirineos.api.movement.dto.response.CashMovementResponse;
import com.lospirineos.api.movement.entity.CashMovement;
import com.lospirineos.api.movement.mapper.CashMovementMapper;
import com.lospirineos.api.movement.repository.CashMovementRepository;
import com.lospirineos.api.movement.service.CashMovementService;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.shift.repository.ShiftRepository;
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
public class CashMovementServiceImpl
        implements CashMovementService {

    private final CashMovementRepository cashMovementRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;

    @Override
    public CashMovementResponse create(
            Long authenticatedUserId,
            CreateCashMovementRequest request
    ) {

        User user = findUser(authenticatedUserId);

        Shift shift = shiftRepository
                .findById(request.shiftId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Turno no encontrado"
                        )
                );

        if (!Boolean.TRUE.equals(shift.getActive())) {
            throw new BusinessException(
                    "El turno seleccionado se encuentra inactivo"
            );
        }

        validateMovement(request);

        CashMovement movement =
                new CashMovement();

        movement.setMovementAt(
                OffsetDateTime.now()
        );

        movement.setShift(shift);
        movement.setUser(user);

        movement.setMovementType(
                request.movementType()
        );

        movement.setConcept(
                request.concept().trim()
        );

        movement.setAccount(
                request.account()
        );

        movement.setBeneficiary(
                request.beneficiary() != null
                        ? request.beneficiary().trim()
                        : null
        );

        movement.setAmount(
                request.amount()
        );

        movement.setPhotoUrl(
                request.photoUrl()
        );

        return CashMovementMapper.toResponse(
                cashMovementRepository.save(movement)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public CashMovementResponse findById(
            Long id
    ) {

        return CashMovementMapper.toResponse(
                findMovement(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashMovementResponse> findAll() {

        return cashMovementRepository
                .findAll()
                .stream()
                .map(CashMovementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashMovementResponse> findMine(
            Long authenticatedUserId
    ) {

        return cashMovementRepository
                .findAllByUserId(authenticatedUserId)
                .stream()
                .map(CashMovementMapper::toResponse)
                .toList();
    }

    private CashMovement findMovement(
            Long id
    ) {

        return cashMovementRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movimiento no encontrado"
                        )
                );
    }

    private User findUser(
            Long id
    ) {

        return userRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario no encontrado"
                        )
                );
    }

    private void validateMovement(
            CreateCashMovementRequest request
    ) {

        if (
                MovementType.PAYMENT.equals(request.movementType())
                        && (
                        request.beneficiary() == null
                                || request.beneficiary().isBlank()
                )
        ) {
            throw new BusinessException(
                    "El beneficiario es obligatorio cuando se registra un pago"
            );
        }
    }
}