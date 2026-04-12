package com.vsms.inventory.service.strategy;

import com.vsms.inventory.entity.SparePart;
import com.vsms.inventory.entity.Vendor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface VendorSelectionStrategy {

    String key();

    Vendor selectVendor(List<Vendor> vendors, SparePart sparePart, Map<Long, BigDecimal> vendorQuotes);
}
