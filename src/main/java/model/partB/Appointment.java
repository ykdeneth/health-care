/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.partB;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Deneth
 */
public class Appointment {
   private final String id;
    private final String patient;
    private final String doctor;
    private final String department;
    private final String facility;
    private final LocalDate date;
    private final LocalTime time;

    public Appointment(String id, String patient, String doctor, String department,
                       String facility, LocalDate date, LocalTime time) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.department = department;
        this.facility = facility;
        this.date = date;
        this.time = time;
    }

    public String getId() { return id; }
    public String getPatient() { return patient; }
    public String getDoctor() { return doctor; }
    public String getDepartment() { return department; }
    public String getFacility() { return facility; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
}
