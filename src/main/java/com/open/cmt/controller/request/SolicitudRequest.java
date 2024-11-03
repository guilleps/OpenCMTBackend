package com.open.cmt.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitudRequest {
    private String nombreCompleto;
    private String identificador;
    private String domicilio;
    private String distrito;
    private String correoElectronico;
    private String telefono;
    private String motivo;
}
