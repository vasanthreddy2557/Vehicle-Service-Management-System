package com.vsms.vehicleservicesystem.inventory.repository;

import com.vsms.vehicleservicesystem.inventory.model.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Long> {
    List<SparePart> findByStockQuantityLessThan(Integer threshold);
}