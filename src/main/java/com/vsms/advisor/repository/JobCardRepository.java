package com.vsms.advisor.repository;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobCardRepository extends JpaRepository<JobCard, Long> {

	List<JobCard> findByStatus(JobCardStatus status);
}
