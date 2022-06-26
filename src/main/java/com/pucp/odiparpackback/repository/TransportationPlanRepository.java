package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.TransportationPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportationPlanRepository extends JpaRepository<TransportationPlan, Long> {
}
