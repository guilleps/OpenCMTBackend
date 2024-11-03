package com.open.cmt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidenteDetalleDTO {
    private String nroIncidente;
    private String fechaHora;
    private String tipoIncidente;
    private String zona;
    private String sector;
    private String tipoIntervencion;
    private String resultadoIntervencion;
    private String detalle;
    private List<PersonalDTO> personal;
    private List<VehiculoDTO> vehiculos;
}
