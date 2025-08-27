/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.math.BigDecimal;

/**
 *
 * @author Deneth
 */
public class BillingSummary {
    private final BigDecimal subTotal;     // sum of line totals
    private final BigDecimal patientPays;  // what patient owes (post insurance math)
    private final BigDecimal insurerPays;  // insurer responsibility
    private final String claimReference;   // null for direct
    private final String note;

    public BillingSummary(BigDecimal subTotal, BigDecimal patientPays, BigDecimal insurerPays, String claimReference, String note) {
        this.subTotal = subTotal;
        this.patientPays = patientPays;
        this.insurerPays = insurerPays;
        this.claimReference = claimReference;
        this.note = note;
    }

    public BigDecimal getSubTotal() { return subTotal; }
    public BigDecimal getPatientPays() { return patientPays; }
    public BigDecimal getInsurerPays() { return insurerPays; }
    public String getClaimReference() { return claimReference; }
    public String getNote() { return note; }
}
