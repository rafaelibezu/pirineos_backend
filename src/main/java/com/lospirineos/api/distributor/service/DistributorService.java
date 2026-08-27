package com.lospirineos.api.distributor.service;

import com.lospirineos.api.distributor.dto.request.CreateDistributorRequest;
import com.lospirineos.api.distributor.dto.request.UpdateDistributorRequest;
import com.lospirineos.api.distributor.dto.response.DistributorResponse;

import java.util.List;

public interface DistributorService {

    DistributorResponse create(
            CreateDistributorRequest request
    );

    DistributorResponse findById(
            Long id
    );

    List<DistributorResponse> findAll();

    List<DistributorResponse> findAllActive();

    DistributorResponse update(
            Long id,
            UpdateDistributorRequest request
    );
}