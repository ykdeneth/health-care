/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partE;

import java.text.SimpleDateFormat;
import java.util.List;
import model.partA.Patient;

/**
 *
 * @author Deneth
 */
public class TreatmentSummaryVisitor implements Visitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visitPatient(Patient patient) {
        report.append("Treatment Summary Report for Patient: ").append(patient.getName()).append("\n");
        report.append("ID: ").append(patient.getId()).append("\n\n");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<String[]> treatments = patient.getTreatmentPlans();
        if (treatments.isEmpty()) {
            report.append("No treatments available.\n");
        } else {
            for (String[] treatment : treatments) {
                // Assuming treatment array format: {startDate, endDate, description, status}
                report.append("Start Date: ").append(treatment[0]).append(", ");
                report.append("End Date: ").append(treatment[1]).append(", ");
                report.append("Description: ").append(treatment[2]).append(", ");
                report.append("Status: ").append(treatment[3]).append("\n");
            }
        }
    }

    @Override
    public String getReport() {
        return report.toString();
    }
}
