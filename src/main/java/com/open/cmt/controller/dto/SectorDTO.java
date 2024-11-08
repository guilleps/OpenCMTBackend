package com.open.cmt.controller.dto;

import com.open.cmt.entity.Sector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectorDTO {
    private String titulo;

    public static SectorDTO fromEntity(Sector sector) {
        return new SectorDTO(sector.getTitulo());
    }
}
