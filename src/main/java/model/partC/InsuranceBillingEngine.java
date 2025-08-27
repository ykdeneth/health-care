/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Deneth
 */
public class InsuranceBillingEngine implements BillingImplementor {

    @Override
    public BillingSummary finalizeBill(String patientId, String insuranceProvider, List<BillingLine> lines) {
        BigDecimal sub = BigDecimal.ZERO;
        for (BillingLine l : lines) {
            sub = sub.add(l.getLineTotal());
        }
        // Example rules. Replace with real tables/rules later.
        BigDecimal coveragePct = deduceCoverage(lines);           // 0.70 meds, 0.80 otherwise
        BigDecimal copay = new BigDecimal("10.00");               // flat example
        BigDecimal insurerPays = sub.multiply(coveragePct);
        BigDecimal patientPays = sub.subtract(insurerPays).add(copay);
        String claimRef = "CLM-" + UUID.randomUUID().toString().substring(0, 8);
        String note = "Insurer=" + insuranceProvider + ", coverage=" + coveragePct.movePointRight(2) + "%, copay=" + copay;
        return new BillingSummary(sub, patientPays.max(BigDecimal.ZERO), insurerPays.max(BigDecimal.ZERO), claimRef, note);
    }

    private BigDecimal deduceCoverage(List<BillingLine> lines) {
        // If any line is Medications, you might apply 70%; otherwise 80% (simplified demo).
        boolean hasMeds = lines.stream().anyMatch(l -> "Medications".equalsIgnoreCase(l.getMainService()));
        return hasMeds ? new BigDecimal("0.70") : new BigDecimal("0.80");
    }
}
