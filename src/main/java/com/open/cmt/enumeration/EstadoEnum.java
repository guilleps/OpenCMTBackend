package com.open.cmt.enumeration;

import java.util.Arrays;

public enum EstadoEnum {
    PENDIENTE,
    ACEPTADO,
    RECHAZADO;

    public static EstadoEnum fromString(String estadoStr) {
        return Arrays.stream(EstadoEnum.values())
                .filter(c -> c.name().equalsIgnoreCase(estadoStr))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado no válido: " + estadoStr));
    }
}
