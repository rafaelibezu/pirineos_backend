package com.lospirineos.api.shift.mapper;

import com.lospirineos.api.shift.dto.response.ShiftResponse;
import com.lospirineos.api.shift.entity.Shift;

public final class ShiftMapper {

    private ShiftMapper() {
    }

    public static ShiftResponse toResponse(
            Shift shift
    ) {

        return new ShiftResponse(
                shift.getId(),
                shift.getCode(),
                shift.getName(),
                shift.getStartTime(),
                shift.getEndTime(),
                shift.getActive()
        );
    }
}