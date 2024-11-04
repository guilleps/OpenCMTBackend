package com.open.cmt.service;

import com.open.cmt.config.email.EmailService;
import com.open.cmt.controller.dto.*;
import com.open.cmt.controller.request.SolicitudRequest;
import com.open.cmt.controller.response.SolicitudResponse;
import com.open.cmt.entity.Incidente;
import com.open.cmt.entity.Solicitante;
import com.open.cmt.entity.Solicitud;
import com.open.cmt.enumeration.EstadoEnum;
import com.open.cmt.enumeration.TimePeriod;
import com.open.cmt.repository.IncidenteRepository;
import com.open.cmt.repository.SolicitudRepository;
import com.open.cmt.service.mapper.SolicitudPreviewMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final IncidenteRepository incidenteRepository;
    private final EmailService emailService;

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

        enviarCorreoConfirmacion(solicitante.getCorreoElectronico(), solicitud.getNroSolicitud());

        return new SolicitudResponse("Solicitud " + solicitud.getNroSolicitud() + " enviada con éxito");
    }

    private void enviarCorreoConfirmacion(String correoElectronico, String nroSolicitud) {
        String subject = "Confirmación de Solicitud N°" + nroSolicitud;
        String body = "Su solicitud ha sido enviada con éxito. Tiene un plazo de 7 días a partir de hoy para recibir una respuesta.";
        emailService.sendEmail(correoElectronico, subject, body);
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public Page<SolicitudDTOPreview> obtenerSolicitudesPorFiltros(EstadoEnum estado, TimePeriod periodo, int page, int size) {
        PageRequest pageRequest = createPageRequest(page, size);
        LocalDateTime fechaInicio = calcularFechaInicio(periodo);
        return solicitudRepository.findByFilters(estado, fechaInicio, pageRequest)
                .map(SolicitudPreviewMapper::toSolicitudDTOPreview);
    }

    @Transactional(readOnly = true)
    public Page<SolicitudDTOPreview> obtenerTodasLasSolicitudesPrevias(int page, int size) {
        PageRequest pageRequest = createPageRequest(page, size);
        return solicitudRepository.findAll(pageRequest)
                .map(SolicitudPreviewMapper::toSolicitudDTOPreview);
    }

    public SolicitudResponse actualizarSolicitud(Long id, EstadoEnum nuevoEstado) {
        Solicitud solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con ID: " + id));

        solicitud.setEstado(nuevoEstado);
        solicitudRepository.save(solicitud);

        return new SolicitudResponse("Solicitud " + solicitud.getNroSolicitud() + " actualizada a estado: " + nuevoEstado);
    }

    private PageRequest createPageRequest(int page, int size) {
        return PageRequest.of(page, size, Sort.Direction.DESC, "fechaHora");
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

    private LocalDateTime calcularFechaInicio(TimePeriod periodo) {
        LocalDateTime now = LocalDateTime.now();
        switch (periodo) {
            case LAST_HOUR -> { return now.minusHours(1); }
            case LAST_24_HOURS -> { return now.minusDays(1); }
            case LAST_WEEK -> { return now.minusWeeks(1); }
            case LAST_MONTH -> { return now.minusMonths(1); }
            case LAST_YEAR -> { return now.minusYears(1); }
            default -> { return null; }
        }
    }

}
