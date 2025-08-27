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
public class PatientElement implements Element {

    private final Patient patient;

    public PatientElement(Patient patient) {
        this.patient = patient;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitPatient(patient);
    }

}
