package com.open.cmt.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SolicitudRequest {
    private String nombreCompleto;

    @Pattern(regexp = "^(\\d{8}|[a-zA-Z0-9]{1,12}|\\d{11})$", message = "Identificador debe ser DNI (8 dígitos), CE (alfanumérico hasta 12 caracteres) o RUC (11 dígitos)")
    private String identificador;
    private String domicilio;
    private String distrito;

    @Email
    private String correoElectronico;

    @Pattern(regexp = "^(9\\d{8}|\\d{7,8})$", message = "Teléfono debe ser un número móvil de 9 dígitos comenzando con 9 o un número fijo de 7 u 8 dígitos")
    private String telefono;
    private String motivo;
}
