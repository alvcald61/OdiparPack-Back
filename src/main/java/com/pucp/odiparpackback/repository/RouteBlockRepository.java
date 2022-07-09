package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.RouteBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RouteBlockRepository extends JpaRepository<RouteBlock, Long> {
  @Query("SELECT r FROM RouteBlock r WHERE (r.startDate > ?1 AND r.startDate < ?2) OR (r.endDate > ?1 AND r.endDate < ?2)" +
          "OR (r.startDate < ?1 AND r.endDate > ?2)")
  List<RouteBlock> findAllBetween(Date startDate, Date endDate);
}
