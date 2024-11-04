package com.open.cmt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTOPreview {
    private String nroSolicitud;
    private String fecha;
    private String hora;
    private String estado;
}
