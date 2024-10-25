package com.open.cmt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentDTO {
    private String nroIncidente;
    private String fecha;
    private String horallamada;
    private String tipoIncidente;
    private String zona;
    private String sector;
    private String tipoIntervencion;
    private String resultado;
}
