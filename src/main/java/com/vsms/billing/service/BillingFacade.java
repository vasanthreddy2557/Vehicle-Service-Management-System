package com.vsms.billing.service;

import com.vsms.advisor.entity.JobCard;
import com.vsms.advisor.repository.JobCardRepository;
import com.vsms.billing.entity.Invoice;
import com.vsms.billing.entity.InvoiceStatus;
import com.vsms.billing.repository.InvoiceRepository;
import com.vsms.inventory.entity.SparePart;
import com.vsms.inventory.repository.SparePartRepository;
import com.vsms.mechanic.entity.ServiceTask;
import com.vsms.mechanic.repository.ServiceTaskRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BillingFacade {

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal("0.18");

    private final JobCardRepository jobCardRepository;
    private final ServiceTaskRepository serviceTaskRepository;
    private final SparePartRepository sparePartRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingFacade(
        JobCardRepository jobCardRepository,
        ServiceTaskRepository serviceTaskRepository,
        SparePartRepository sparePartRepository,
        InvoiceRepository invoiceRepository
    ) {
        this.jobCardRepository = jobCardRepository;
        this.serviceTaskRepository = serviceTaskRepository;
        this.sparePartRepository = sparePartRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // Facade Pattern: exposes one method that coordinates labor, parts, tax, and invoice persistence.
    // SOLID: follows SRP because clients avoid coupling to multiple billing calculation collaborators.
    @Transactional
    public Invoice generateFinalInvoice(Long jobCardId, Map<Long, Integer> partQuantities, BigDecimal taxRate) {
        JobCard jobCard = jobCardRepository.findById(jobCardId)
            .orElseThrow(() -> new IllegalArgumentException("JobCard not found: " + jobCardId));

        List<ServiceTask> serviceTasks = serviceTaskRepository.findByJobCardId(jobCardId);
        BigDecimal laborTotal = serviceTasks.stream()
            .map(ServiceTask::getLaborCost)
            .filter(cost -> cost != null)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal partsTotal = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : partQuantities.entrySet()) {
            SparePart sparePart = sparePartRepository.findById(entry.getKey())
                .orElseThrow(() -> new IllegalArgumentException("SparePart not found: " + entry.getKey()));
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            partsTotal = partsTotal.add(sparePart.getUnitCost().multiply(quantity));
        }

        BigDecimal effectiveTaxRate = taxRate == null ? DEFAULT_TAX_RATE : taxRate;
        BigDecimal subtotal = laborTotal.add(partsTotal);
        BigDecimal taxAmount = subtotal.multiply(effectiveTaxRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal grandTotal = subtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        Invoice invoice = invoiceRepository.findByJobCardId(jobCardId).orElseGet(Invoice::new);
        invoice.setJobCard(jobCard);
        invoice.setLaborTotal(laborTotal.setScale(2, RoundingMode.HALF_UP));
        invoice.setPartsTotal(partsTotal.setScale(2, RoundingMode.HALF_UP));
        invoice.setTaxAmount(taxAmount);
        invoice.setGrandTotal(grandTotal);
        invoice.setStatus(InvoiceStatus.ISSUED);

        return invoiceRepository.save(invoice);
    }
}
