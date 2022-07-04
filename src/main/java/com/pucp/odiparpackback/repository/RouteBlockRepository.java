package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.RouteBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface RouteBlockRepository extends JpaRepository<RouteBlock, Long> {
  List<RouteBlock> findAllByStartDateBetweenOrEndDateBetween(Date startDate1, Date endDate1, Date startDate2, Date endDate2);
}
