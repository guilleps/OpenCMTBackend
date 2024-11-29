package com.open.cmt.repository;

import com.open.cmt.entity.Incidente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface IncidenteRepository extends JpaRepository<Incidente, Integer> {
    Page<Incidente> findAll(Pageable pageable);

    @Query("SELECT i FROM Incidente i " +
            "WHERE (:fecha IS NULL OR i.fecha = :fecha) " +
            "AND (:zona IS NULL OR i.zona.nombre = :zona) " +
            "AND (:sector IS NULL OR i.sector.titulo = :sector) " +
            "AND (:tipoIncidente IS NULL OR i.tipoIncidente.nombre = :tipoIncidente)")
    Page<Incidente> findByFilters(@Param("fecha") LocalDate fecha,
                                  @Param("zona") String zona,
                                  @Param("sector") String sector,
                                  @Param("tipoIncidente") String tipoIncidente,
                                  Pageable pageable);

}
