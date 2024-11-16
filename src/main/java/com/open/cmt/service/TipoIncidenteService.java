package com.open.cmt.service;

import com.open.cmt.controller.dto.TipoIncidenteDTO;
import com.open.cmt.entity.TipoIncidente;
import com.open.cmt.repository.TipoIncidenteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TipoIncidenteService {
    private final TipoIncidenteRepository tipoIncidenteRepository;


    public List<TipoIncidenteDTO> getAllIncidentType() {
        List<TipoIncidente> tipoIncidentes = tipoIncidenteRepository.findAll();
        return tipoIncidentes.stream()
                .map(TipoIncidenteDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
