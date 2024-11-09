package com.open.cmt.controller.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudDTO {
    private String nroSolicitud;
    private String fechaSolicitud;
    private String horaSolicitud;
    private String solicitante;
    private String identificacion;
    private String domicilio;
    private String correoElectronico;
    private String telefono;
    private String motivo;
    private String nroIncidente;
    private String estado;
}
