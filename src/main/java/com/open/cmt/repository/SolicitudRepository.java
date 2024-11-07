package com.open.cmt.repository;

import com.open.cmt.entity.Solicitud;
import com.open.cmt.enumeration.EstadoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    @Query(value = "SELECT s.nro_solicitud FROM solicitud s WHERE s.nro_solicitud LIKE CONCAT(:fecha, '%') ORDER BY s.nro_solicitud DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastSolicitudNumber(String fecha);

    Page<Solicitud> findAll(Pageable pageable);

    @Query(value = "SELECT s FROM Solicitud s " +
            "WHERE (:estado IS NULL OR s.estado = :estado) " +
            "AND (:fechaInicio IS NULL OR s.fechaHora >= :fechaInicio)")
    Page<Solicitud> findByFilters(@Param("estado") EstadoEnum estado,
                                  @Param("fechaInicio") LocalDateTime fechaInicio,
                                  Pageable pageable);

    Optional<Solicitud> findSolicitudByNroSolicitud(String nroSolicitud);

}

