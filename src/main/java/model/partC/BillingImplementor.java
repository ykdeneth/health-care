/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.util.List;

/**
 *
 * @author Deneth
 */
public interface BillingImplementor {
    BillingSummary finalizeBill(String patientId, String insuranceProvider, List<BillingLine> lines);
}
