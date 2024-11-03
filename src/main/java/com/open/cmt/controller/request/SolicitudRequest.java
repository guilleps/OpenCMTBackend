package com.open.cmt.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitudDTO {
    private String nombreCompleto;
    private String identificador;
    private String domicilio;
    private String distrito;
    private String correoElectronico;
    private String telefono;
    private String motivo;
}
