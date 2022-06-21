package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Historic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface HistoricRepository extends JpaRepository<Historic, Integer> {
    List<Historic> findAllByOrderDateBetween(Date startDate, Date endDate);
}
