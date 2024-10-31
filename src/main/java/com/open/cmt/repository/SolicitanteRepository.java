package com.open.cmt.repository;

import com.open.cmt.entity.Solicitante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitanteRepository extends JpaRepository<Solicitante, Long> {
}
