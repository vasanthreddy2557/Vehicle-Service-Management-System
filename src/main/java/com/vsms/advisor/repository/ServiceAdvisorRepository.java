package com.vsms.advisor.repository;

import com.vsms.advisor.entity.ServiceAdvisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceAdvisorRepository extends JpaRepository<ServiceAdvisor, Long> {
}
