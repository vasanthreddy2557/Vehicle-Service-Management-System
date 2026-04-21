package com.vsms.mechanic.repository;

import com.vsms.mechanic.entity.Mechanic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
}
