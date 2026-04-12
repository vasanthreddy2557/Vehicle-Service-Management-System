package com.vsms.inventory.service;

import com.vsms.inventory.entity.PurchaseOrder;
import com.vsms.inventory.entity.PurchaseOrderStatus;
import com.vsms.inventory.entity.SparePart;
import com.vsms.inventory.entity.Vendor;
import com.vsms.inventory.repository.PurchaseOrderRepository;
import com.vsms.inventory.repository.SparePartRepository;
import com.vsms.inventory.repository.VendorRepository;
import com.vsms.inventory.service.strategy.VendorSelectionStrategy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final SparePartRepository sparePartRepository;
    private final VendorRepository vendorRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final Map<String, VendorSelectionStrategy> strategies;

    public InventoryService(
        SparePartRepository sparePartRepository,
        VendorRepository vendorRepository,
        PurchaseOrderRepository purchaseOrderRepository,
        List<VendorSelectionStrategy> strategies
    ) {
        this.sparePartRepository = sparePartRepository;
        this.vendorRepository = vendorRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.strategies = strategies.stream().collect(Collectors.toMap(VendorSelectionStrategy::key, Function.identity()));
    }

    @Transactional
    public PurchaseOrder generatePurchaseOrder(
        Long sparePartId,
        Integer quantity,
        String strategyKey,
        Map<Long, BigDecimal> vendorQuotes
    ) {
        SparePart sparePart = sparePartRepository.findById(sparePartId)
            .orElseThrow(() -> new IllegalArgumentException("SparePart not found: " + sparePartId));

        List<Vendor> vendors = vendorRepository.findAll();
        VendorSelectionStrategy strategy = resolveStrategy(strategyKey);
        Vendor selectedVendor = strategy.selectVendor(vendors, sparePart, vendorQuotes);

        BigDecimal unitPrice = vendorQuotes.getOrDefault(selectedVendor.getId(), sparePart.getUnitCost());
        BigDecimal totalCost = unitPrice.multiply(BigDecimal.valueOf(quantity));

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSparePart(sparePart);
        purchaseOrder.setVendor(selectedVendor);
        purchaseOrder.setQuantityOrdered(quantity);
        purchaseOrder.setUnitPrice(unitPrice);
        purchaseOrder.setTotalCost(totalCost);
        purchaseOrder.setExpectedDeliveryDate(LocalDate.now().plusDays(selectedVendor.getLeadTimeInDays()));
        purchaseOrder.setStatus(PurchaseOrderStatus.CREATED);

        return purchaseOrderRepository.save(purchaseOrder);
    }

    public List<SparePart> getAllSpareParts() {
        return sparePartRepository.findAll();
    }

    public List<SparePart> getLowStockSpareParts(Integer threshold) {
        return sparePartRepository.findByQuantityInStockLessThanEqual(threshold);
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    private VendorSelectionStrategy resolveStrategy(String strategyKey) {
        VendorSelectionStrategy strategy = strategies.get(strategyKey == null ? "FASTEST" : strategyKey.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported strategy: " + strategyKey);
        }
        return strategy;
    }
}
