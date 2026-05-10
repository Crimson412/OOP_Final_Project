package oop.dao;

import oop.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles all database operations for Patient objects.
 *
 * <p>
 * This class is the DAO for the patients table. It hides the SQL statements
 * from the service and main layers so the rest of the program can work with
 * Patient objects instead of database rows.
 * </p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class PatientDAO {
    /**
     * Adds a new patient to the database.
     *
     * @param patient the patient to be added to the database
     * */
    public static void addPatient(Patient patient) {
        String sql = "INSERT INTO patients (patient_id, name, date_of_birth, contact_info) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, patient.getPatientID());
            stmt.setString(2, patient.getName());
            stmt.setString(3, patient.getDateOfBirth());
            stmt.setString(4, patient.getContactInfo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not add patient.", e);
        }
    }


    /**
     * Gets every patient currently stored in the database.
     *
     * @return an ArrayList of all saved patients
     */
    public static ArrayList<Patient> getAllPatients() {
        String sql = "SELECT patient_id, name, date_of_birth, contact_info FROM patients ORDER BY patient_id";
        ArrayList<Patient> patients = new ArrayList<>();

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
     * Finds a patient using their ID number.
     *
     * @param patientID the ID of the patient to search for
     * @return Patient object containing the patient if found, or null otherwise
     */
    public static Patient getPatientByID(long patientID) {
        String sql = "SELECT patient_id, name, date_of_birth, contact_info FROM patients WHERE patient_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, patientID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPatient(rs);
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Could not get patient.", e);
        }
    }

    /**
     * Deletes a patient from the database.
     *
     * <p>
     * The database uses foreign keys, so this will fail if the patient still
     * has appointments connected to them.
     * </p>
     *
     * @param patientID the ID of the patient to delete
     */
    public static void deletePatient(long patientID) {
        String sql = "DELETE FROM patients WHERE patient_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, patientID);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Patient not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete patient. This patient may still have appointments.", e);
        }
    }

    /**
     * Converts the current database row into a Patient object.
     *
     * @param rs the result set positioned on a patient row
     * @return the mapped Patient object
     * @throws SQLException if a column cannot be read from the result set
     */
    private static Patient mapPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getLong("patient_id"),
                rs.getString("name"),
                rs.getString("date_of_birth"),
                rs.getString("contact_info")
        );
    }
}
