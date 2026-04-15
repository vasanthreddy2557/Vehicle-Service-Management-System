package com.vsms.billing.entity;

import com.vsms.advisor.entity.JobCard;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "job_card_id", nullable = false, unique = true)
    private JobCard jobCard;

    private BigDecimal laborTotal;

    private BigDecimal partsTotal;

    private BigDecimal taxAmount;

    private BigDecimal grandTotal;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
