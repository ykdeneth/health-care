/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partC;

/**
 *
 * @author Deneth
 */
public class BillingEngineSelector {
    public static BillingAbstraction newSession(String patientId, String insuranceProvider) {
        BillingImplementor impl = (insuranceProvider == null || insuranceProvider.isBlank())
                ? new DirectPayBillingEngine()
                : new InsuranceBillingEngine();
        return new BillingAbstraction(impl, patientId, insuranceProvider);
    }
}
