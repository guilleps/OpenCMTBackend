package com.open.cmt.controller;

import com.open.cmt.controller.dto.IncidenteDetalleDTO;
import com.open.cmt.controller.dto.SolicitudDTO;
import com.open.cmt.controller.dto.SolicitudDTOPreview;
import com.open.cmt.controller.response.SolicitudResponse;
import com.open.cmt.enumeration.EstadoEnum;
import com.open.cmt.enumeration.TimePeriod;
import com.open.cmt.exception.InvalidActionException;
import com.open.cmt.service.SolicitudService;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/{nroSolicitud}")
    public ResponseEntity<?> obtenerSolicitud(@PathVariable String nroSolicitud) {
        SolicitudDTO solicitudDTO = solicitudService.obtenerSolicitud(nroSolicitud);
        return ResponseEntity.ok(solicitudDTO);
    }

    /**
     * Endpoint para atender una solicitud especificada por su ID.
     * La solicitud puede aprobarse o rechazarse en función del parámetro "accion".
     *
     * @param nroSolicitud el nroSolicitud de la solicitud a atender
     * @param accion       la acción a realizar, "approve" o "reject"
     * @return una respuesta con el estado actualizado de la solicitud
     */
    @PatchMapping("/{nroSolicitud}/atender")
    public ResponseEntity<SolicitudResponse> atenderSolicitud(@PathVariable String nroSolicitud,
                                              @RequestParam String accion) throws MessagingException, IOException {
        if (!accion.equalsIgnoreCase("approve") && !accion.equalsIgnoreCase("reject")) {
            throw new InvalidActionException("Accion incorrecta");
        }

        SolicitudResponse response = "approve".equalsIgnoreCase(accion)
                ? solicitudService.aprobarSolicitud(nroSolicitud)
                : solicitudService.rechazarSolicitud(nroSolicitud);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{nroSolicitud}/incidente")
    public ResponseEntity<IncidenteDetalleDTO> obtenerIncidenteSolicitud(@PathVariable String nroSolicitud) {
        IncidenteDetalleDTO incidenteDetalleDTO = solicitudService.obtenerIncidenteDetalle(nroSolicitud);
        return ResponseEntity.ok(incidenteDetalleDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<SolicitudDTOPreview>>> obtenerSolicitudesPorFiltros(
            @RequestParam(required = false) String estadoStr,
            @RequestParam(required = false) String periodoStr,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            PagedResourcesAssembler<SolicitudDTOPreview> assembler) {

        EstadoEnum estadoEnum = estadoStr != null ? EstadoEnum.fromString(estadoStr) : null;
        TimePeriod periodoEnum = periodoStr != null ? TimePeriod.fromString(periodoStr) : TimePeriod.ALL_TIME;

        Page<SolicitudDTOPreview> solicitudes = solicitudService.obtenerSolicitudesPorFiltros(
                estadoEnum, periodoEnum, page, size);
        return ResponseEntity.ok(assembler.toModel(solicitudes));
    }

    @GetMapping("/todos")
    public ResponseEntity<PagedModel<EntityModel<SolicitudDTOPreview>>> obtenerTodasLasSolicitudesPrevias(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            PagedResourcesAssembler<SolicitudDTOPreview> assembler) {

        Page<SolicitudDTOPreview> solicitudes = solicitudService.obtenerTodasLasSolicitudesPrevias(page, size);
        return ResponseEntity.ok(assembler.toModel(solicitudes));
    }

}
