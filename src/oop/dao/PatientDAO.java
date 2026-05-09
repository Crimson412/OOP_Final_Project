package oop.dao;

import oop.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all database operations for Patient objects.
 *
 * <p>This class is the DAO for the patients table. It hides the SQL statements
 * from the service and main layers so the rest of the program can work with
 * Patient objects instead of database rows.</p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class PatientDAO {
    private static final long PATIENT_ID_BASE = 900000000L;

    /**
     * Adds a new patient to the database.
     *
     * @param name the patient's name
     * @param dateOfBirth the patient's date of birth
     * @param contactInfo the patient's contact information
     * @return the Patient object that was saved to the database
     */
    public Patient addPatient(String name, String dateOfBirth, String contactInfo) {
        long id = getNextPatientId();
        String sql = "INSERT INTO patients (patient_id, name, date_of_birth, contact_info) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setString(3, dateOfBirth);
            stmt.setString(4, contactInfo);
            stmt.executeUpdate();

            return new Patient(name, dateOfBirth, contactInfo);
        } catch (SQLException e) {
            throw new RuntimeException("Could not add patient.", e);
        }
    }

    /**
     * Finds a patient using their ID number.
     *
     * @param patientId the ID of the patient to search for
     * @return an Optional containing the patient if found, or empty otherwise
     */
    public Optional<Patient> getPatientById(long patientId) {
        String sql = "SELECT patient_id, name, date_of_birth, contact_info FROM patients WHERE patient_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapPatient(rs));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Could not get patient.", e);
        }
    }

    /**
     * Gets every patient currently stored in the database.
     *
     * @return a list of all saved patients
     */
    public List<Patient> getAllPatients() {
        String sql = "SELECT patient_id, name, date_of_birth, contact_info FROM patients ORDER BY patient_id";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patients.add(mapPatient(rs));
            }

            return patients;
        } catch (SQLException e) {
            throw new RuntimeException("Could not list patients.", e);
        }
    }

    /**
     * Deletes a patient from the database.
     *
     * <p>The database uses foreign keys, so this will fail if the patient still
     * has appointments connected to them.</p>
     *
     * @param patientId the ID of the patient to delete
     */
    public void deletePatient(long patientId) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, patientId);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Patient not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete patient. This patient may still have appointments.", e);
        }
    }

    /**
     * Checks whether a patient exists in the database.
     *
     * @param patientId the patient ID to check
     * @return true if the patient exists, false otherwise
     */
    public boolean exists(long patientId) {
        return getPatientById(patientId).isPresent();
    }

    /**
     * Creates the next patient ID using the largest ID currently stored.
     *
     * @return the next available patient ID
     */
    private long getNextPatientId() {
        String sql = "SELECT COALESCE(MAX(patient_id), ?) + 1 AS next_id FROM patients";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, PATIENT_ID_BASE);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("next_id");
                }
            }

            return PATIENT_ID_BASE + 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create next patient ID.", e);
        }
    }

    /**
     * Converts the current database row into a Patient object.
     *
     * @param rs the result set positioned on a patient row
     * @return the mapped Patient object
     * @throws SQLException if a column cannot be read from the result set
     */
    private Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getString("name"),
                rs.getString("date_of_birth"),
                rs.getString("contact_info")
        );
    }
}
