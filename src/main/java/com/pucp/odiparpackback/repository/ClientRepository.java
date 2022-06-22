package com.pucp.odiparpackback.repository;

import com.pucp.odiparpackback.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
  Optional<Client> findByRuc(String ruc);
}
