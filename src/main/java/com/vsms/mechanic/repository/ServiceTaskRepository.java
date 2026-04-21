package com.vsms.mechanic.repository;

import com.vsms.mechanic.entity.ServiceTask;
import com.vsms.mechanic.entity.ServiceTaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTaskRepository extends JpaRepository<ServiceTask, Long> {

	List<ServiceTask> findByJobCardId(Long jobCardId);

	List<ServiceTask> findByStatus(ServiceTaskStatus status);

	boolean existsByJobCardIdAndStatusNot(Long jobCardId, ServiceTaskStatus status);
}
