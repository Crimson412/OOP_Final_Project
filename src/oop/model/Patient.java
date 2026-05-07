package oop.model;

//import java.time.LocalDate;

/**
 * Patient class storing their ID, name, DOB, and contact info.
 *
 * @author Noah W.
 * @version Final Project, model
 * @bugs None
 */
public class Patient {
    private static int numPatients = 0;
    // ideally this is set to a length that will accomidate all patients for appearance purposes
    // for now we will use the classic 900 number that nmt uses - Rheggeth
    private long patientID = 900000000;
    private String name;
    private String dateOfBirth;
    private String contactInfo;


    /**
     * Default constructor that creates a patient with all variables provided.
     *
     * @param name String of the patients name
     * @param dateOfBirth String for the patients birth date
     * @param contactInfo String for the patients contact info
     */
    public Patient(String name, String dateOfBirth, String contactInfo) {
        numPatients++;
        // Validation
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");

        // Then we create a unique ID by adding the total number of patients to the default ID - Rheggeth
        patientID += numPatients;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters.

    public long getPatientID() { return patientID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dob) { dateOfBirth = dob; }

    public String getContactInfo() { return contactInfo; }

    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    /**
     * Returns a string representation of this patient.
     * @return a string describing this patient
     */
    @Override
    public String toString() {
        return String.format("Patient [ID: %d, Name: %s, DOB: %s]", patientID, name, dateOfBirth);
    }
}
