package com.open.cmt.service;

import com.open.cmt.controller.dto.IncidentDTO;
import com.open.cmt.controller.dto.IncidenteDTOPreview;
import com.open.cmt.controller.dto.SectorDTO;
import com.open.cmt.controller.dto.ZonaDTO;
import com.open.cmt.entity.Incidente;
import com.open.cmt.exception.ResourceNotFoundException;
import com.open.cmt.repository.IncidenteRepository;
import com.open.cmt.service.mapper.IncidentMapper;
import com.open.cmt.service.mapper.IncidentePreviewMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidenteService {
    private final IncidenteRepository incidenteRepository;
    private final ZonaService zonaService;
    private final SectorService sectorService;

    private PageRequest createPageRequest(int page, int size, Sort and) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha")
                .and(Sort.by(Sort.Direction.DESC, "horallamada")));
    }

    @Transactional(readOnly = true)
    public Page<IncidenteDTOPreview> obtenerIncidentesPorFiltros(LocalDate fecha, String zona, String sector, String tipoIncidente, int page, int size) {
        PageRequest pageRequest = createPageRequest(page, size, Sort.by(Sort.Direction.DESC, "fecha")
                .and(Sort.by(Sort.Direction.DESC, "horallamada")));

        if (fecha == null && zona == null && sector == null && tipoIncidente == null) {
            return incidenteRepository.findAll(pageRequest)
                    .map(IncidentePreviewMapper::toIncidenteDTOPreview);
        }

        return incidenteRepository.findByFilters(fecha, zona, sector, tipoIncidente, pageRequest)
                .map(IncidentePreviewMapper::toIncidenteDTOPreview);
    }

    @Transactional(readOnly = true)
    public Page<IncidenteDTOPreview> obtenerTodosLosIncidentesPrevio(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fecha")
                .and(Sort.by(Sort.Direction.DESC, "horallamada")));

        return incidenteRepository.findAll(pageRequest)
                .map(IncidentePreviewMapper::toIncidenteDTOPreview);
    }

    @Transactional(readOnly = true)
    public IncidentDTO obtenerDetalleDeIncidente(Long id) {
        return incidenteRepository.findById(id)
                .map(IncidentMapper::toIncidenteDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Incidente con id: " + id + " no encontrado"));
    }

    public Incidente buscarIncidentePorId(Long id) {
        return incidenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Incidente no encontrado con ID: " + id));
    }

    public List<ZonaDTO> obtenerZonas() {
        return zonaService.getAllZonas();
    }

    public List<SectorDTO> obtenerSectoresDeZona(String zona) {
        return sectorService.getAllSectorsByZone(zona);
    }
}
