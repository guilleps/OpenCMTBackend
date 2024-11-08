package com.open.cmt.controller.dto;

import com.open.cmt.entity.Zona;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonaDTO {
    private String nombre;

    public static ZonaDTO fromEntity(Zona zona) {
        return new ZonaDTO(zona.getNombre());
    }
}
