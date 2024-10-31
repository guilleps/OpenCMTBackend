package com.open.cmt.entity;

import com.open.cmt.enumeration.EstadoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nro_solicitud")
    private String nroSolicitud;

    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String motivo;

    @ManyToOne
    @JoinColumn(name = "idsolicitante", nullable = false)
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "idincidente")
    private Incidente incidente;
}

