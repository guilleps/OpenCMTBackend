package com.open.cmt.entity;

import com.open.cmt.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    private String usuario;
    private String contrasenia;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Role role;
}
