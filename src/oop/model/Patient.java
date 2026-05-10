package oop.model;

/**
 * Patient class storing their ID, name, DOB, and contact info.
 *
 * @author Noah W.
 * @version Final Project, model
 * @bugs None
 */
public class Patient {
    private static int numPatients = 0;
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

        // Then we create a unique ID by adding the total number of patients to the default ID - Rheggeth
        patientID += numPatients;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
    }

    /**
     * Constructor used when we already know the patients ID, when we load in from sql database.
     *
     * @param id the patients id
     * @param name the patients name
     * @param DOB the patients date of birth
     * @param contact the patients contact info
     */
    public Patient(long id, String name, String DOB, String contact) {
        patientID = id;
        this.name = name;
        dateOfBirth = DOB;
        contactInfo = contact;
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
     * Method that updates the number of patients given the last ID found in the database.
     *
     * @bugs Hard coding the 900# feels pretty bad but patientID is not static
     */
    public static void updateNumPatients(long lastID) {
        numPatients = (int)(lastID - 900000000);
    }

    /**
     * Returns a string representation of this patient.
     *
     * @return a string describing this patient
     */
    @Override
    public String toString() {
        return String.format("Patient [ID: %d, Name: %s, DOB: %s, Contact: %s]", patientID, name, dateOfBirth, contactInfo);
    }
}
