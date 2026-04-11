package com.vsms.inventory.controller;

import com.vsms.inventory.service.InventoryService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("spareParts", inventoryService.getAllSpareParts());
        model.addAttribute("lowStockSpareParts", inventoryService.getLowStockSpareParts(5));
        model.addAttribute("purchaseOrders", inventoryService.getAllPurchaseOrders());
        return "inventory-dashboard";
    }

    @PostMapping("/order")
    public String generatePurchaseOrder(
        @RequestParam Long sparePartId,
        @RequestParam Integer quantity,
        @RequestParam(required = false) String strategyKey,
        @RequestParam Map<String, String> requestParams
    ) {
        Map<Long, BigDecimal> vendorQuotes = extractVendorQuotes(requestParams);
        inventoryService.generatePurchaseOrder(sparePartId, quantity, strategyKey, vendorQuotes);
        return "redirect:/inventory";
    }

    private Map<Long, BigDecimal> extractVendorQuotes(Map<String, String> requestParams) {
        Map<Long, BigDecimal> vendorQuotes = new HashMap<>();
        for (Map.Entry<String, String> entry : requestParams.entrySet()) {
            if (entry.getKey().startsWith("vendor_")) {
                Long vendorId = Long.valueOf(entry.getKey().substring("vendor_".length()));
                vendorQuotes.put(vendorId, new BigDecimal(entry.getValue()));
            }
        }
        return vendorQuotes;
    }
}
