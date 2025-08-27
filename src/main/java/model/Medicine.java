 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Deneth
 */
public class Medicine {
    private String drugId;
    private String name;
    private double unitPrice;
    private int quantity;
    private String supplier;

    public Medicine(String drugId, String name, double unitPrice, int quantity, String supplier) {
        this.drugId = drugId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.supplier = supplier;
    }

    // Getters and setters below (optional)...

    public String getDrugId() { return drugId; }
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }
    public String getSupplier() { return supplier; }
}
