package com.open.cmt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidente")
public class Incidente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private LocalTime horallamada;

    @ManyToOne
    @JoinColumn(name = "idtipoincidente", referencedColumnName = "id")
    private TipoIncidente tipoIncidente;

    @Column(name = "tipo_intervencion")
    private String tipoIntervencion;

    private String resultado;

    private String detalle;

    @ManyToOne
    @JoinColumn(name = "idzona", referencedColumnName = "id")
    private Zona zona;

    @ManyToOne
    @JoinColumn(name = "idsector", referencedColumnName = "id")
    private Sector sector;

    private String idoperador;

    // Relación muchos a muchos con personal a través de la tabla incidente_personal
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "incidente_personal", // Tabla intermedia
            joinColumns = @JoinColumn(name = "id_incidente"), // Clave foránea hacia Incidente
            inverseJoinColumns = @JoinColumn(name = "id_personal") // Clave foránea hacia Personal
    )
    private List<Personal> personalList;

    // Relación muchos a muchos con vehiculo a través de la tabla incidente_vehiculo
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "incidente_vehiculo", // Tabla intermedia
            joinColumns = @JoinColumn(name = "id_incidente"), // Clave foránea hacia Incidente
            inverseJoinColumns = @JoinColumn(name = "id_vehiculo") // Clave foránea hacia Vehiculo
    )
    private List<Vehiculo> vehiculos;

    private String horallegada;
    private String direccion;

}
