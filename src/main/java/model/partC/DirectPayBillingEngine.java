/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Deneth
 */
public class DirectPayBillingEngine implements BillingImplementor {
    @Override
    public BillingSummary finalizeBill(String patientId, String insuranceProvider, List<BillingLine> lines) {
        BigDecimal sub = BigDecimal.ZERO;
        for (BillingLine l : lines) {
            sub = sub.add(l.getLineTotal());
        }
        return new BillingSummary(sub, sub, BigDecimal.ZERO, null, "Direct pay");
    }
}
