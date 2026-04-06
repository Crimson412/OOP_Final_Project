package oop.appointment_scheduling_system;

import java.time.LocalDate;

/**
* Patient class storing their ID, name, DOB, and contact info
*
* @author Noah W.
* @version Final Project, Scheduling System
* @bugs None
*/
public class Patient {
    private String patientId;
    private String name;
    private LocalDate dateOfBirth;
    private String contactInfo;

    public Patient(String patientId, String name, LocalDate dateOfBirth, String contactInfo) {
        // Validation
        if (patientId == null || patientId.isEmpty()) throw new IllegalArgumentException("Patient ID cannot be empty.");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");

        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    @Override
    public String toString() {
        return String.format("Patient [ID: %s, Name: %s, DOB: %s]", patientId, name, dateOfBirth);
    }
}
