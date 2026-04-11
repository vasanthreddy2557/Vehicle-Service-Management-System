package com.vsms.inventory.service.strategy;

import com.vsms.inventory.entity.SparePart;
import com.vsms.inventory.entity.Vendor;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FastestVendorStrategy implements VendorSelectionStrategy {

    @Override
    public String key() {
        return "FASTEST";
    }

    @Override
    public Vendor selectVendor(List<Vendor> vendors, SparePart sparePart, Map<Long, BigDecimal> vendorQuotes) {
        return vendors.stream()
            .min(Comparator.comparing(Vendor::getLeadTimeInDays))
            .orElseThrow(() -> new IllegalArgumentException("No vendors available"));
    }
}
