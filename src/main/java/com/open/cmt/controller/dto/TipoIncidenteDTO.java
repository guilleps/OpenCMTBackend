package com.open.cmt.controller.dto;

import com.open.cmt.entity.TipoIncidente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoIncidenteDTO {
    private String nombre;

    public static TipoIncidenteDTO fromEntity(TipoIncidente entity) {
        return new TipoIncidenteDTO(entity.getNombre());
    }
}
