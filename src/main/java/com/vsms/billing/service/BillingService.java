package com.vsms.billing.service;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.entity.JobCardStatus;
import com.vsms.advisor.repository.JobCardRepository;
import com.vsms.billing.entity.Invoice;
import com.vsms.billing.repository.InvoiceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

    private final BillingFacade billingFacade;
    private final JobCardRepository jobCardRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingService(BillingFacade billingFacade, JobCardRepository jobCardRepository, InvoiceRepository invoiceRepository) {
        this.billingFacade = billingFacade;
        this.jobCardRepository = jobCardRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Transactional
    public Invoice generateInvoice(Long jobCardId, Map<Long, Integer> partQuantities, BigDecimal taxRate) {
        return billingFacade.generateFinalInvoice(jobCardId, partQuantities, taxRate);
    }

    public List<JobCard> getReadyForBillingJobCards() {
        return jobCardRepository.findByStatus(JobCardStatus.READY_FOR_BILLING);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }
}
