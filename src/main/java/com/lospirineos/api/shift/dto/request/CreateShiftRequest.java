package com.lospirineos.api.shift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record CreateShiftRequest(

        @NotBlank(message = "El código del turno es obligatorio")
        @Size(max = 50, message = "El código no puede superar los 50 caracteres")
        String code,

        @NotBlank(message = "El nombre del turno es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime
) {
}