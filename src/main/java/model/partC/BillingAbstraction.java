/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Deneth
 */
public class BillingAbstraction {
    private final BillingImplementor implementor;
    private final String patientId;
    private final String insuranceProvider;
    private final List<BillingLine> lines = new ArrayList<>();

    public BillingAbstraction(BillingImplementor implementor, String patientId, String insuranceProvider) {
        this.implementor = implementor;
        this.patientId = patientId;
        this.insuranceProvider = insuranceProvider;
    }

    public void addLine(BillingLine line) { lines.add(line); }
    public List<BillingLine> getLines() { return Collections.unmodifiableList(lines); }

    public BillingSummary submit() {
        return implementor.finalizeBill(patientId, insuranceProvider, lines);
    }
}
