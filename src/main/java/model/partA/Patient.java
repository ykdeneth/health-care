
package model.partA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Deneth
 */
@Entity
@Table(name = "patients")
public class Patient implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Temporal(TemporalType.DATE)
    private Date dob;
    private String gender;
    private String bloodGroup;
    private String contact;
    private String address;
    @Column(nullable = true)
    private List<String[]> medicalHistory = new ArrayList<>();
    @Column(nullable = true)
    private List<String[]> treatmentPlans = new ArrayList<>();

    // Getters and setters for all fields...
    public int getId(){ return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Date getDOB() { return dob; }
    public void setDOB(Date dob) { this.dob = dob; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress(){return address;}
    public void setAddress(String address){this.address = address;}
    public List<String[]> getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(List<String[]> medicalHistory) { this.medicalHistory = medicalHistory; }
    public List<String[]> getTreatmentPlans() { return treatmentPlans; }
    public void setTreatmentPlans(List<String[]> treatmentPlans) { this.treatmentPlans = treatmentPlans; }

    public PatientMemento saveToMemento() {
        // Defensive copy of mutable lists before saving
        List<String[]> medHistCopy = new ArrayList<>(medicalHistory);
        List<String[]> treatPlanCopy = new ArrayList<>(treatmentPlans);
        return new PatientMemento(id, name, dob, gender, bloodGroup, contact, address, medHistCopy, treatPlanCopy);
    }

    public void restoreFromMemento(PatientMemento memento) {
        this.id = memento.getId();
        this.name = memento.getName();
        this.dob = memento.getDOB();
        this.gender = memento.getGender();
        this.bloodGroup = memento.getBloodGroup();
        this.contact = memento.getContact();
        this.address = memento.getAddress();
        this.medicalHistory = memento.getMedicalHistory();
        this.treatmentPlans = memento.getTreatmentPlans();
    }
}
