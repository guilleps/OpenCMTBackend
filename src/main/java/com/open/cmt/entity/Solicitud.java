package com.open.cmt.entity;

import com.open.cmt.enumeration.EstadoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitante")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nro_solicitud")
    private String nroSolicitud;

    private EstadoEnum estado;
    private String tiempo;
    private String motivo;
}
