package com.open.cmt.service.mapper;

import com.open.cmt.controller.dto.IncidentDTO;
import com.open.cmt.entity.Incidente;

import java.time.format.DateTimeFormatter;

public class IncidentMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static IncidentDTO toIncidenteDTO(Incidente incidente) {
        // Formatear manualmente los valores
        String formattedDate = incidente.getFecha() != null ? incidente.getFecha().format(dateFormatter) : null;
        String formattedTime = incidente.getHorallamada() != null ? incidente.getHorallamada().format(timeFormatter) : null;

        return new IncidentDTO(
                incidente.getId().toString(),
                formattedDate,
                formattedTime,
                incidente.getTipoIncidente() != null ? incidente.getTipoIncidente().getNombre() : null,
                incidente.getZona() != null ? incidente.getZona().getNombre() : null,
                incidente.getSector() != null ? incidente.getSector().getTitulo() : null,
                incidente.getTipoIntervencion(),
                incidente.getResultado()
        );
    }
}
