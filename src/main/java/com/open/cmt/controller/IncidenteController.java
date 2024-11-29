package com.open.cmt.controller;

import com.open.cmt.controller.dto.IncidentDTO;
import com.open.cmt.controller.dto.IncidenteDTOPreview;
import com.open.cmt.controller.dto.SectorDTO;
import com.open.cmt.controller.dto.TipoIncidenteDTO;
import com.open.cmt.controller.request.SolicitudRequest;
import com.open.cmt.controller.response.SolicitudResponse;
import com.open.cmt.service.IncidenteService;
import com.open.cmt.service.SolicitudService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/incidente")
@RequiredArgsConstructor
public class IncidenteController {
    private final IncidenteService incidenteService;
    private final SolicitudService solicitudService;

    @GetMapping("/buscar")
    public ResponseEntity<?> obtenerIncidentesConFiltros(
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) String zona,
            @RequestParam(required = false) String sector,
            @RequestParam(required = false) String tipoIncidente,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Max(100) int size,
            PagedResourcesAssembler<IncidenteDTOPreview> assembler) {

        LocalDate fechaParsed = (fecha != null && !fecha.isEmpty()) ? LocalDate.parse(fecha) : null;
        Page<IncidenteDTOPreview> incidentes = incidenteService.obtenerIncidentesPorFiltros(fechaParsed, zona, sector, tipoIncidente, page, size);
        return ResponseEntity.ok(assembler.toModel(incidentes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerIncidentePorId(@PathVariable Integer id) {
        IncidentDTO incidenteDTO = incidenteService.obtenerDetalleDeIncidente(id);
        return ResponseEntity.ok(EntityModel.of(incidenteDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IncidenteController.class).obtenerIncidentePorId(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(IncidenteController.class).obtenerIncidentesConFiltros(null, null, null, null, 0, 10, null)).withRel("todos")));
    }

    @PostMapping("/{id}/solicitar")
    public ResponseEntity<SolicitudResponse> solicitarAccesoIncidente(@PathVariable Integer id, @RequestBody SolicitudRequest solicitudRequest) {
        SolicitudResponse solicitudResponse = solicitudService.crearSolicitud(solicitudRequest, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudResponse);
    }

    @GetMapping
    public ResponseEntity<List<SectorDTO>> obtenerSectorPorZona(@RequestParam("zona") String zona) {
        List<SectorDTO> sectores = incidenteService.obtenerSectoresDeZona(zona);
        return ResponseEntity.ok(sectores);
    }

    @GetMapping("/tipo-incidente")
    public ResponseEntity<List<TipoIncidenteDTO>> obtenerIncidentes() {
        List<TipoIncidenteDTO> tipoIncidentes = incidenteService.obtenerTiposDeIncidente();
        return ResponseEntity.ok(tipoIncidentes);
    }
}
