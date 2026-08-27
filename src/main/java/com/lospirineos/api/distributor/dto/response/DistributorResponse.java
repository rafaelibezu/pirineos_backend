package com.lospirineos.api.distributor.dto.response;

public record DistributorResponse(

        Long id,
        String name,
        String description,
        Boolean active
) {
}