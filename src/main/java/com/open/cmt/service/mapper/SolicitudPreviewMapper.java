package com.open.cmt.service.mapper;

import com.open.cmt.controller.dto.SolicitudDTOPreview;
import com.open.cmt.entity.Solicitud;

import java.time.format.DateTimeFormatter;

public class SolicitudPreviewMapper {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static SolicitudDTOPreview toSolicitudDTOPreview(Solicitud solicitud) {
        // Formatear manualmente los valores
        String formattedDate = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(dateFormatter) : null;
        String formattedTime = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(timeFormatter) : null;

        return new SolicitudDTOPreview(
                solicitud.getNroSolicitud(),
                formattedDate,
                formattedTime,
                solicitud.getEstado().name()
        );
    }
}
