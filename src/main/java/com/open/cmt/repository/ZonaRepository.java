package com.open.cmt.repository;

import com.open.cmt.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Integer> {

    List<Zona> findAllByOrderByIdAsc();
}
