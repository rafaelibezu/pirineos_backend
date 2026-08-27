package com.lospirineos.api.shift.repository;

import com.lospirineos.api.shift.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    Optional<Shift> findByCode(String code);

    List<Shift> findAllByActiveTrue();

    boolean existsByCode(String code);
}