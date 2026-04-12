package com.vsms.inventory.service.strategy;

import com.vsms.inventory.entity.SparePart;
import com.vsms.inventory.entity.Vendor;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CheapestVendorStrategy implements VendorSelectionStrategy {

    @Override
    public String key() {
        return "CHEAPEST";
    }

    // Strategy Pattern: algorithm for vendor selection is interchangeable at runtime.
    // SOLID: follows OCP because new vendor-selection algorithms can be added without editing InventoryService.
    @Override
    public Vendor selectVendor(List<Vendor> vendors, SparePart sparePart, Map<Long, BigDecimal> vendorQuotes) {
        return vendors.stream()
            .min(Comparator.comparing(vendor -> vendorQuotes.getOrDefault(vendor.getId(), sparePart.getUnitCost())))
            .orElseThrow(() -> new IllegalArgumentException("No vendors available"));
    }
}
