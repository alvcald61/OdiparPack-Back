package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.RouteBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteBlockRepository extends JpaRepository<RouteBlock, Long> {
}
