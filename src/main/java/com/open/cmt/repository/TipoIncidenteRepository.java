package com.open.cmt.repository;

import com.open.cmt.entity.TipoIncidente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoIncidenteRepository extends JpaRepository<TipoIncidente, Integer> {
    List<TipoIncidente> findAllByOrderByNombreAsc();
}
