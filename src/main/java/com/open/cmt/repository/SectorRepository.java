package com.open.cmt.repository;

import com.open.cmt.entity.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Integer> {
    List<Sector> findAllByZonaNombre(String zonaNombre);
}
