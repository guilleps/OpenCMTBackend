package com.open.cmt.controller;

import com.open.cmt.controller.dto.IncidenteDetalleDTO;
import com.open.cmt.controller.dto.SolicitudDTO;
import com.open.cmt.service.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/solicitudes")
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
}
