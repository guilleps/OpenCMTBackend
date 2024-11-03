package com.open.cmt.repository;

import com.open.cmt.entity.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
    @Query(value = "SELECT s.nro_solicitud FROM solicitud s WHERE s.nro_solicitud LIKE CONCAT(:fecha, '%') ORDER BY s.nro_solicitud DESC LIMIT 1", nativeQuery = true)
    Optional<String> findLastSolicitudNumber(String fecha);
}

