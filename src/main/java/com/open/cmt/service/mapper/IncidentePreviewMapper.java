package com.open.cmt.service.mapper;

import com.open.cmt.controller.dto.IncidenteDTOPreview;
import com.open.cmt.entity.Incidente;

import java.time.format.DateTimeFormatter;

public class IncidentePreviewMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static IncidenteDTOPreview toIncidenteDTOPreview(Incidente incidente) {
        // Formatear manualmente los valores
        String formattedDate = incidente.getFecha() != null ? incidente.getFecha().format(dateFormatter) : null;
        String formattedTime = incidente.getHorallamada() != null ? incidente.getHorallamada().format(timeFormatter) : null;

        return new IncidenteDTOPreview(
                incidente.getId().toString(),
                formattedDate,
                formattedTime,
                incidente.getTipoIncidente().getNombre()
        );
    }
}
