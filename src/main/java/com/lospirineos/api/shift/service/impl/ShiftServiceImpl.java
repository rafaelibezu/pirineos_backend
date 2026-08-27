package com.lospirineos.api.shift.service.impl;

import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.shift.dto.request.CreateShiftRequest;
import com.lospirineos.api.shift.dto.request.UpdateShiftRequest;
import com.lospirineos.api.shift.dto.response.ShiftResponse;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.shift.mapper.ShiftMapper;
import com.lospirineos.api.shift.repository.ShiftRepository;
import com.lospirineos.api.shift.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ShiftServiceImpl
        implements ShiftService {

    private final ShiftRepository shiftRepository;

    @Override
    public ShiftResponse create(
            CreateShiftRequest request
    ) {

        if (
                shiftRepository
                        .existsByCode(request.code().trim().toUpperCase())
        ) {
            throw new BusinessException(
                    "Ya existe un turno con ese código"
            );
        }

        Shift shift = new Shift();

        shift.setCode(
                request.code()
                        .trim()
                        .toUpperCase()
        );

        shift.setName(
                request.name().trim()
        );

        shift.setStartTime(
                request.startTime()
        );

        shift.setEndTime(
                request.endTime()
        );

        shift.setActive(true);

        return ShiftMapper.toResponse(
                shiftRepository.save(shift)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ShiftResponse findById(
            Long id
    ) {

        return ShiftMapper.toResponse(
                findShift(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponse> findAll() {

        return shiftRepository
                .findAll()
                .stream()
                .map(ShiftMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShiftResponse> findAllActive() {

        return shiftRepository
                .findAllByActiveTrue()
                .stream()
                .map(ShiftMapper::toResponse)
                .toList();
    }

    @Override
    public ShiftResponse update(
            Long id,
            UpdateShiftRequest request
    ) {

        Shift shift =
                findShift(id);

        shift.setName(
                request.name().trim()
        );

        shift.setStartTime(
                request.startTime()
        );

        shift.setEndTime(
                request.endTime()
        );

        if (request.active() != null) {
            shift.setActive(
                    request.active()
            );
        }

        return ShiftMapper.toResponse(
                shiftRepository.save(shift)
        );
    }

    private Shift findShift(
            Long id
    ) {

        return shiftRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Turno no encontrado"
                        )
                );
    }
}