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
import com.open.cmt.repository.SolicitudRepository;
import com.open.cmt.service.mapper.SolicitudPreviewMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SolicitudService {
    private final SolicitudRepository solicitudRepository;
    private final IncidenteService incidenteService;
    private final EmailService emailService;
    private final GeneratedPDFService pdfService;

    public SolicitudResponse crearSolicitud(SolicitudRequest solicitudRequest, Long id) {

        Solicitante solicitante = Solicitante.builder()
                .nombreCompleto(solicitudRequest.getNombreCompleto())
                .identificador(solicitudRequest.getIdentificador())
                .domicilio(solicitudRequest.getDomicilio())
                .distrito(solicitudRequest.getDistrito())
                .correoElectronico(solicitudRequest.getCorreoElectronico())
                .telefono(solicitudRequest.getTelefono())
                .build();

        Incidente incidente = incidenteService.buscarIncidentePorId(id);

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
        String body = "Su solicitud ha sido enviada con éxito.\n\n" +
                "De acuerdo con el Artículo 7 de la Ley N° 27806, a partir de hoy se inicia el plazo de siete (7) días hábiles para recibir una respuesta formal a su solicitud.\n\n" +
                "Atentamente,\n\n" +
                "OpenCMT";
        emailService.sendEmail(correoElectronico, subject, body);
    }

    public SolicitudDTO obtenerSolicitud(String nroSolicitud) {
        Solicitud solicitud = solicitudRepository.findSolicitudByNroSolicitud(nroSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con N°" + nroSolicitud));

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
    public IncidenteDetalleDTO obtenerIncidenteDetalle(String nroSolicitud) {
        Solicitud solicitud = solicitudRepository.findSolicitudByNroSolicitud(nroSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con N°" + nroSolicitud));
        Long idIncidente = solicitud.getIncidente().getId().longValue();

        Incidente incidente = incidenteService.buscarIncidentePorId(idIncidente);

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

    public Solicitud buscarSoliciturPorId(Long id) {
        return solicitudRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con ID: " + id));
    }

    @Transactional
    public SolicitudResponse aprobarSolicitud(String nroSolicitud) throws MessagingException, IOException {
        Solicitud solicitud = solicitudRepository.findSolicitudByNroSolicitud(nroSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con N°" + nroSolicitud));

        solicitud.setEstado(EstadoEnum.ACEPTADO);
        solicitudRepository.save(solicitud);

        Incidente incidente = solicitud.getIncidente();
        incidente.getPersonalList().size();

        enviarDatosDeSolicitudAprobada(solicitud.getSolicitante().getCorreoElectronico(), solicitud.getNroSolicitud(), solicitud, incidente);

        return new SolicitudResponse("Solicitud " + solicitud.getNroSolicitud() + " actualizada a estado: " + solicitud.getEstado().toString());
    }

    private void enviarDatosDeSolicitudAprobada(String correoElectronico, String nroSolicitud, Solicitud solicitud, Incidente incidente) throws MessagingException, IOException {
        String subject = "Solicitud N°" + nroSolicitud + " APROBADA";
        String body = "Estimado(a) " + solicitud.getSolicitante().getNombreCompleto() + ",\n\n" +
                "Nos complace informarle que su solicitud N°" + nroSolicitud + " ha sido aprobada exitosamente.\n\n" +
                "Adjunto a este correo encontrará un documento PDF con la información completa de su solicitud y los detalles del incidente asociado.\n\n" +
                "Agradecemos su paciencia durante el proceso de revisión.\n\n" +
                "Atentamente,\n" +
                "OpenCMT";

        byte[] pdfBytes = pdfService.generarPDFSolicitud(solicitud, incidente);
        emailService.sendEmailWithAttachment(correoElectronico, subject, body, pdfBytes, "Solicitud_" + nroSolicitud + ".pdf");
    }

    public SolicitudResponse rechazarSolicitud(String nroSolicitud) {
        Solicitud solicitud = solicitudRepository.findSolicitudByNroSolicitud(nroSolicitud)
                .orElseThrow(() -> new EntityNotFoundException("Solicitud no encontrado con N°" + nroSolicitud));

        solicitud.setEstado(EstadoEnum.RECHAZADO);
        solicitudRepository.save(solicitud);

        enviarDatosDeSolicitudRechazada(solicitud.getSolicitante().getCorreoElectronico(), solicitud.getNroSolicitud());

        return new SolicitudResponse("Solicitud " + solicitud.getNroSolicitud() + " actualizada a estado: " + solicitud.getEstado().toString());
    }

    private void enviarDatosDeSolicitudRechazada(String correoElectronico, String nroSolicitud) {
        String subject = "Solicitud N°" + nroSolicitud + " RECHAZADA";
        String body = "Lamentamos informarle que su solicitud ha sido rechazada, ya que ha solicitado material videográfico, fotográfico u otro relacionado con el incidente, el cual no puede ser entregado a una persona natural.\n\nAtentamente,\nOpenCMT";
        emailService.sendEmail(correoElectronico, subject, body);
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
            case LAST_HOUR -> {
                return now.minusHours(1);
            }
            case LAST_24_HOURS -> {
                return now.minusDays(1);
            }
            case LAST_WEEK -> {
                return now.minusWeeks(1);
            }
            case LAST_MONTH -> {
                return now.minusMonths(1);
            }
            case LAST_YEAR -> {
                return now.minusYears(1);
            }
            default -> {
                return null;
            }
        }
    }

}
