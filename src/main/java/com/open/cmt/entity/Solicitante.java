package com.open.cmt.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "solicitante")
public class Solicitante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    private String identificador;
    private String domicilio;
    private String distrito;

    @Column(name = "correo_electronico")
    private String correoElectronico;

    private String telefono;

    @OneToMany(mappedBy = "solicitante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Solicitud> solicitudes;
}
