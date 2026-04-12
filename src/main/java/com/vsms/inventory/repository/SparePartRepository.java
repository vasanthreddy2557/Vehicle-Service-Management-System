package com.vsms.inventory.repository;

import com.vsms.inventory.entity.SparePart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SparePartRepository extends JpaRepository<SparePart, Long> {

	List<SparePart> findByQuantityInStockLessThanEqual(Integer threshold);
}
