package com.lospirineos.api.distributor.service.impl;

import com.lospirineos.api.common.exception.BusinessException;
import com.lospirineos.api.common.exception.ResourceNotFoundException;
import com.lospirineos.api.distributor.dto.request.CreateDistributorRequest;
import com.lospirineos.api.distributor.dto.request.UpdateDistributorRequest;
import com.lospirineos.api.distributor.dto.response.DistributorResponse;
import com.lospirineos.api.distributor.entity.Distributor;
import com.lospirineos.api.distributor.mapper.DistributorMapper;
import com.lospirineos.api.distributor.repository.DistributorRepository;
import com.lospirineos.api.distributor.service.DistributorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DistributorServiceImpl
        implements DistributorService {

    private final DistributorRepository distributorRepository;

    @Override
    public DistributorResponse create(
            CreateDistributorRequest request
    ) {

        if (
                distributorRepository
                        .existsByNameIgnoreCase(request.name())
        ) {
            throw new BusinessException(
                    "Ya existe un distribuidor con ese nombre"
            );
        }

        Distributor distributor =
                new Distributor();

        distributor.setName(
                request.name().trim()
        );

        distributor.setDescription(
                request.description()
        );

        distributor.setActive(true);

        return DistributorMapper.toResponse(
                distributorRepository.save(distributor)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public DistributorResponse findById(
            Long id
    ) {

        return DistributorMapper.toResponse(
                findDistributor(id)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributorResponse> findAll() {

        return distributorRepository
                .findAll()
                .stream()
                .map(DistributorMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistributorResponse> findAllActive() {

        return distributorRepository
                .findAllByActiveTrue()
                .stream()
                .map(DistributorMapper::toResponse)
                .toList();
    }

    @Override
    public DistributorResponse update(
            Long id,
            UpdateDistributorRequest request
    ) {

        Distributor distributor =
                findDistributor(id);

        distributorRepository
                .findByNameIgnoreCase(request.name())
                .filter(existing ->
                        !existing.getId().equals(id)
                )
                .ifPresent(existing -> {
                    throw new BusinessException(
                            "Ya existe un distribuidor con ese nombre"
                    );
                });

        distributor.setName(
                request.name().trim()
        );

        distributor.setDescription(
                request.description()
        );

        if (request.active() != null) {
            distributor.setActive(
                    request.active()
            );
        }

        return DistributorMapper.toResponse(
                distributorRepository.save(distributor)
        );
    }

    private Distributor findDistributor(
            Long id
    ) {

        return distributorRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Distribuidor no encontrado"
                        )
                );
    }
}