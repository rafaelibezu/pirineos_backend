package com.lospirineos.api.shift.dto.response;

import java.time.LocalTime;

public record ShiftResponse(
        Long id,
        String code,
        String name,
        LocalTime startTime,
        LocalTime endTime,
        Boolean active
) {
}