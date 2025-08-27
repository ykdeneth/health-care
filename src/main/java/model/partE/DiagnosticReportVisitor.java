/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partE;

import java.util.List;
import model.partA.Patient;

/**
 *
 * @author Deneth
 */
public class DiagnosticReportVisitor implements Visitor {

    private final StringBuilder report = new StringBuilder();

    @Override
    public void visitPatient(Patient patient) {
        report.append("Diagnostic Report for Patient: ").append(patient.getName()).append("\n");
        report.append("ID: ").append(patient.getId()).append("\n\n");

        List<String[]> medicalHistory = patient.getMedicalHistory();
        if (medicalHistory.isEmpty()) {
            report.append("No diagnostic history available.\n");
        } else {
            for (String[] diag : medicalHistory) {
                // Assuming diagnostic history array format: {date, condition, doctorNotes}
                report.append("Date: ").append(diag[0]).append(", ");
                report.append("Condition: ").append(diag[1]).append(", ");
                report.append("Doctor Note: ").append(diag[2]).append("\n");
            }
        }
    }

    @Override
    public String getReport() {
        return report.toString();
    }
}
