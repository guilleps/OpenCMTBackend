package com.open.cmt.service.mapper;

import com.open.cmt.controller.dto.SolicitudDTO;
import com.open.cmt.entity.Solicitante;
import com.open.cmt.entity.Solicitud;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SolicitudMapper {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static SolicitudDTO toSolicitudDTO(Solicitud solicitud) {
        // Formatear manualmente los valores
        String formattedDate = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(dateFormatter) : null;
        String formattedTime = solicitud.getFechaHora() != null ? solicitud.getFechaHora().format(timeFormatter) : null;

        return new SolicitudDTO(
                solicitud.getNroSolicitud(),
                formatFecha(solicitud),
                formatHora(solicitud),
                Optional.ofNullable(solicitud.getSolicitante()).map(Solicitante::getNombreCompleto).orElse(null),
                Optional.ofNullable(solicitud.getSolicitante()).map(Solicitante::getIdentificador).orElse(null),
                Optional.ofNullable(solicitud.getSolicitante()).map(Solicitante::getDomicilio).orElse(null),
                Optional.ofNullable(solicitud.getSolicitante()).map(Solicitante::getCorreoElectronico).orElse(null),
                Optional.ofNullable(solicitud.getSolicitante()).map(Solicitante::getTelefono).orElse(null),
                solicitud.getMotivo(),
                Optional.ofNullable(solicitud.getIncidente()).map(i -> i.getId().toString()).orElse(null),
                solicitud.getEstado().toString()
        );
    }

    private static String formatFecha(Solicitud solicitud) {
        return Optional.ofNullable(solicitud.getFechaHora())
                .map(fecha -> fecha.format(dateFormatter))
                .orElse(null);
    }

    private static String formatHora(Solicitud solicitud) {
        return Optional.ofNullable(solicitud.getFechaHora())
                .map(fecha -> fecha.format(timeFormatter))
                .orElse(null);
    }
}
