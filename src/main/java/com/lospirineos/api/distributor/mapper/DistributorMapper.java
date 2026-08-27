package com.lospirineos.api.distributor.mapper;

import com.lospirineos.api.distributor.dto.response.DistributorResponse;
import com.lospirineos.api.distributor.entity.Distributor;

public final class DistributorMapper {

    private DistributorMapper() {
    }

    public static DistributorResponse toResponse(
            Distributor distributor
    ) {

        return new DistributorResponse(
                distributor.getId(),
                distributor.getName(),
                distributor.getDescription(),
                distributor.getActive()
        );
    }
}