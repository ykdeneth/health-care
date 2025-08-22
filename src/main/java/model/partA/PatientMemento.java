package model.partA;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Deneth
 */
public class PatientMemento {

    private final int id;
    private final String name;
    private final Date dob;
    private final String gender;
    private final String bloodGroup;
    private final String contact;
    private final String address;
    private final List<String[]> medicalHistory;
    private final List<String[]> treatmentPlans;

    public PatientMemento(int id, String name, Date dob, String gender, String bloodGroup, String contact, String address,
            List<String[]> medicalHistory, List<String[]> treatmentPlans) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.contact = contact;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.treatmentPlans = treatmentPlans;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDOB() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getContact() {
        return contact;
    }

    public String getAddress() {
        return address;
    }

    public List<String[]> getMedicalHistory() {
        return medicalHistory;
    }

    public List<String[]> getTreatmentPlans() {
        return treatmentPlans;
    }
}
