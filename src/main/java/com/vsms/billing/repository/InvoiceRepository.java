package com.vsms.billing.repository;

import com.vsms.billing.entity.Invoice;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	Optional<Invoice> findByJobCardId(Long jobCardId);
}
