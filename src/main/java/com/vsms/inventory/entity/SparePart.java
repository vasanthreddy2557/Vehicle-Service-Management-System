package com.vsms.inventory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "spare_parts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partCode;

    private String partName;

    private Integer quantityInStock;

    private Integer reorderLevel;

    private BigDecimal unitCost;

    @ManyToOne(optional = false)
    @JoinColumn(name = "preferred_vendor_id", nullable = false)
    private Vendor preferredVendor;

    @Version
    private Integer version;
}
