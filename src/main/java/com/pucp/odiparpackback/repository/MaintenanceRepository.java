package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
  List<Maintenance> getAllByInitialDate(Date initialDate);
}
