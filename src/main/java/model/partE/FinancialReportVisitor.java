/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.partA.Patient;
import model.partC.BillingSummaryStore;
import model.partC.BillingSummaryStore.BillingRecord;

/**
 *
 * @author Deneth
 */
public class FinancialReportVisitor implements Visitor {

    private final StringBuilder report = new StringBuilder();

    private final Date fromDate;
    private final Date toDate;

    public FinancialReportVisitor(Date fromDate, Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public void visitPatient(Patient patient) {
        report.append("Financial Report for Patient: ").append(patient.getName()).append("\n");
        report.append("ID: ").append(patient.getId()).append("\n\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        report.append("From: ").append(sdf.format(fromDate)).append(", To: ").append(sdf.format(toDate)).append("\n\n");

        List<BillingRecord> records = BillingSummaryStore.getBillingSummaries(String.valueOf(patient.getId()));

        if (records.isEmpty()) {
            report.append("No billing records available.\n");
        } else {
            for (BillingRecord record : records) {
                Date date = record.getDate();
                if (!date.before(fromDate) && !date.after(toDate)) {
                    report.append("Date: ").append(sdf.format(date)).append("\n");
                    report.append("Subtotal: ").append(record.getSummary().getSubTotal()).append("\n");
                    report.append("Patient Pays: ").append(record.getSummary().getPatientPays()).append("\n");
                    report.append("Insurer Pays: ").append(record.getSummary().getInsurerPays()).append("\n");
                    report.append("Claim Ref: ").append(record.getSummary().getClaimReference()).append("\n\n");
                }
            }
        }
    }

    @Override
    public String getReport() {
        return report.toString();
    }
}
