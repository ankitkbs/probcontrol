package com.seaexplorer.repository;

import com.seaexplorer.model.VisitedPosition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitedPositionRepository extends JpaRepository<VisitedPosition, Long> {
}
