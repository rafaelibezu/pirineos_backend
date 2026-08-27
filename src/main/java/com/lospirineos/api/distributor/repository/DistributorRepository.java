package com.lospirineos.api.distributor.repository;

import com.lospirineos.api.distributor.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {

    Optional<Distributor> findByNameIgnoreCase(String name);

    List<Distributor> findAllByActiveTrue();

    boolean existsByNameIgnoreCase(String name);
}