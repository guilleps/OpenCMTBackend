package com.open.cmt.service;

import com.open.cmt.controller.dto.IncidenteDetalleDTO;
import com.open.cmt.controller.dto.PersonalDTO;
import com.open.cmt.controller.dto.SolicitudDTO;
import com.open.cmt.controller.dto.VehiculoDTO;
import com.open.cmt.controller.request.SolicitudRequest;
import com.open.cmt.controller.response.SolicitudResponse;
import com.open.cmt.entity.Incidente;
import com.open.cmt.entity.Solicitante;
import com.open.cmt.entity.Solicitud;
import com.open.cmt.enumeration.EstadoEnum;
import com.open.cmt.repository.IncidenteRepository;
import com.open.cmt.repository.SolicitudRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final IncidenteRepository incidenteRepository;

    public SolicitudResponse crearSolicitud(SolicitudRequest solicitudRequest, Long id) {

        Solicitante solicitante = Solicitante.builder()
                .nombreCompleto(solicitudRequest.getNombreCompleto())
                .identificador(solicitudRequest.getIdentificador())
                .domicilio(solicitudRequest.getDomicilio())
                .distrito(solicitudRequest.getDistrito())
                .correoElectronico(solicitudRequest.getCorreoElectronico())
                .telefono(solicitudRequest.getTelefono())
                .build();

        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incidente no encontrado con ID: " + id));

        Solicitud solicitud = Solicitud.builder()
                .nroSolicitud(generarNroSolicitud())
                .solicitante(solicitante)
                .estado(EstadoEnum.PENDIENTE)
                .fechaHora(LocalDateTime.now())
                .motivo(solicitudRequest.getMotivo())
                .incidente(incidente)
                .build();

        solicitante.setSolicitud(solicitud);

        solicitudRepository.save(solicitud);

        return new SolicitudResponse("Solicitud " + solicitud.getNroSolicitud() + " enviada con éxito");
    }

    public SolicitudDTO obtenerSolicitud(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con ID: " + id));

        return SolicitudDTO.builder()
                .nroSolicitud(solicitud.getNroSolicitud())
                .fechaSolicitud(solicitud.getFechaHora().toString())
                .solicitante(solicitud.getSolicitante().getNombreCompleto())
                .identificacion(solicitud.getSolicitante().getIdentificador())
                .domicilio(solicitud.getSolicitante().getDomicilio())
                .correoElectronico(solicitud.getSolicitante().getCorreoElectronico())
                .telefono(solicitud.getSolicitante().getTelefono())
                .motivo(solicitud.getMotivo())
                .nroIncidente(solicitud.getIncidente().getId().toString())
                .build();
    }

    @Transactional
    public IncidenteDetalleDTO obtenerIncidenteDetalle(Long id) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrada con ID: " + id));
        Long idIncidente = solicitud.getIncidente().getId().longValue();

        Incidente incidente = incidenteRepository.findById(idIncidente)
                .orElseThrow(() -> new EntityNotFoundException("Incidente no encontrado con ID: " + idIncidente));

        return IncidenteDetalleDTO.builder()
                .nroIncidente(incidente.getId().toString())
                .fechaHora(incidente.getFecha().toString())
                .tipoIncidente(incidente.getTipoIncidente().getNombre())
                .zona(incidente.getZona().getNombre())
                .sector(incidente.getSector().getTitulo())
                .tipoIntervencion(incidente.getTipoIntervencion())
                .resultadoIntervencion(incidente.getResultado())
                .detalle(incidente.getDetalle())
                .personal(incidente.getPersonalList().stream()
                        .map(personal -> PersonalDTO.builder()
                                .nombreCompleto(personal.getNombre())
                                .cargo(personal.getCargo().getNombre())
                                .build())
                        .toList())
                .vehiculos(incidente.getVehiculos().stream()
                        .map(vehiculo -> VehiculoDTO.builder()
                                .numero(vehiculo.getNumero())
                                .placa(vehiculo.getPlaca())
                                .build())
                        .toList())
                .build();
    }

    private String generarNroSolicitud() {
        String fechaActual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Optional<String> lastSolicitud = solicitudRepository.findLastSolicitudNumber(fechaActual);
        int nextNumber = 1;

        if (lastSolicitud.isPresent()) {
            String lastNumberStr = lastSolicitud.get().substring(9);
            nextNumber = Integer.parseInt(lastNumberStr) + 1;
        }

        return fechaActual + "-" + String.format("%03d", nextNumber);
    }

}
