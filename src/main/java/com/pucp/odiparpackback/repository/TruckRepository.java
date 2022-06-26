package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.utils.TruckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
  List<Truck> findByAvailableTrue();

  Truck findTruckByCode(String code);

  List<Truck> findAllByStatusLessThanEqual(TruckStatus status);
}
