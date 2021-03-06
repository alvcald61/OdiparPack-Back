package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Truck;
import com.pucp.odiparpackback.utils.TruckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
  Truck findTruckByCode(String code);

  List<Truck> findAllByStatusLessThanEqual(TruckStatus status);

  List<Truck> findAllByStatus(TruckStatus status);

  @Query(value = "SELECT * FROM truck WHERE status = 0 OR status = 2", nativeQuery = true)
  List<Truck> findAllByStatus_StoppedAndStatus_Available();

}
