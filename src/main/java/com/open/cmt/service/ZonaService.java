package com.open.cmt.service;

import com.open.cmt.controller.dto.ZonaDTO;
import com.open.cmt.entity.Zona;
import com.open.cmt.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ZonaService {
    private final ZonaRepository zonaRepository;

    public List<ZonaDTO> getAllZonas() {
        List<Zona> zonas = zonaRepository.findAllByOrderByIdAsc();
        return zonas.stream()
                .map(ZonaDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
