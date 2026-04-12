package com.vsms.customer.repository;

import com.vsms.customer.entity.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	Optional<Customer> findByVehicleNumber(String vehicleNumber);
}
