/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 * @author Deneth
 */
public class BillingLine {
    private final String mainService;   // "Consultations" | "Treatments" | "Medications"
    private final String description;   // includes extra like "(Surgeries)" or "(Diagnostics)" when needed
    private final int quantity;
    private final BigDecimal unitPrice;

    public BillingLine(String mainService, String description, int quantity, BigDecimal unitPrice) {
        this.mainService = Objects.requireNonNull(mainService);
        this.description = Objects.requireNonNull(description);
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.quantity = quantity;
        this.unitPrice = Objects.requireNonNull(unitPrice);
    }

    public String getMainService() { return mainService; }
    public String getDescription() { return description; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
