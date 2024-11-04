package com.open.cmt.controller;

import com.open.cmt.controller.dto.IncidenteDetalleDTO;
import com.open.cmt.controller.dto.SolicitudDTO;
import com.open.cmt.controller.dto.SolicitudDTOPreview;
import com.open.cmt.controller.response.SolicitudResponse;
import com.open.cmt.enumeration.EstadoEnum;
import com.open.cmt.enumeration.TimePeriod;
import com.open.cmt.service.SolicitudService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudDTO> obtenerSolicitud(@PathVariable Long id) {
        SolicitudDTO solicitudDTO = solicitudService.obtenerSolicitud(id);
        return ResponseEntity.ok(solicitudDTO);
    }

    @GetMapping("/{id}/incidente")
    public ResponseEntity<IncidenteDetalleDTO> obtenerIncidenteSolicitud(@PathVariable Long id) {
        IncidenteDetalleDTO incidenteDetalleDTO = solicitudService.obtenerIncidenteDetalle(id);
        return ResponseEntity.ok(incidenteDetalleDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PagedModel<EntityModel<SolicitudDTOPreview>>> obtenerSolicitudesPorFiltros (
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

    @PatchMapping("/{id}/atender")
    public ResponseEntity<SolicitudResponse> atenderSolicitud(@PathVariable Long id,
                                                              @RequestParam String nuevoEstado) {
        EstadoEnum estadoEnum = nuevoEstado != null ? EstadoEnum.fromString(nuevoEstado) : null;
        SolicitudResponse response = solicitudService.actualizarSolicitud(id, estadoEnum);
        return ResponseEntity.ok(response);
    }

}
