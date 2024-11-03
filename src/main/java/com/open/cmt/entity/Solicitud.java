package com.open.cmt.entity;

import com.open.cmt.enumeration.EstadoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitud")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nro_solicitud", unique = true)
    private String nroSolicitud;

    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

    @Column(name = "fecha_hora")
    private LocalDateTime fechaHora;

    private String motivo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "solicitante_id", referencedColumnName = "id", nullable = false, unique = true)
    private Solicitante solicitante;

    @ManyToOne
    @JoinColumn(name = "idincidente")
    private Incidente incidente;
}

