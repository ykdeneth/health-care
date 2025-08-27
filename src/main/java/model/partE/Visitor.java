/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partE;

import model.partA.Patient;

/**
 *
 * @author Deneth
 */
public interface Visitor {
    void visitPatient(Patient patient);
    String getReport();  // Returns generated report as String
}
