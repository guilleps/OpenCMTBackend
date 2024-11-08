package com.open.cmt.service;

import com.open.cmt.controller.dto.SectorDTO;
import com.open.cmt.entity.Sector;
import com.open.cmt.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SectorService {
    private final SectorRepository sectorRepository;

    public List<SectorDTO> getAllSectorsByZone(String zona) {
        List<Sector> sectores = sectorRepository.findAllByZonaNombre(zona);
        return sectores.stream()
                .map(SectorDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
