package oop.appointment_scheduling_system;

//import java.time.LocalDate;

/**
* Patient class storing their ID, name, DOB, and contact info
*
* @author Noah W.
* @version Final Project, Scheduling System
* @bugs None
*/
public class Patient {
    private static int numPatients = 0;
    // ideally this is set to a length that will accomidate all patients for appearance purposes
    // for now we will use the classic 900 number that nmt uses - Rheggeth
    private long patientId = 900000000;
    private String name;
    private String dateOfBirth;
    private String contactInfo;

    public Patient(String name, String dateOfBirth, String contactInfo) {
        numPatients++;
        // Validation
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");

        // Then we create a unique ID by adding the total number of patients to the default ID - Rheggeth
        patientId += numPatients;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public long getPatientId() { return patientId; }
    // No setter for patient ID - Rheggeth

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dob) { dateOfBirth = dob; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    @Override
    public String toString() {
        return String.format("Patient [ID: %d, Name: %s, DOB: %s]", patientId, name, dateOfBirth);
    }
}
