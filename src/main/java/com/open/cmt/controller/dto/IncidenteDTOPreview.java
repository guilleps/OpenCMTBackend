package com.open.cmt.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenteDTOPreview {
    private String nroIncidente;
    private String fecha;
    private String hora;
    private String tipoIncidente;
}
