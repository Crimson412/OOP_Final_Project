package oop.dao;

import oop.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles all database operations for Appointment objects.
 *
 * <p>
 * This class is the DAO for the appointments table. It also joins the
 * patients and providers tables so appointment rows can be mapped back into full
 * Appointment objects.
 * </p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class AppointmentDAO {
    private static final String BASE_SELECT =
        "SELECT a.appointment_id, a.patient_id, a.provider_id, a.start_time, a.end_time, a.reason, a.status, " +
        "p.name AS patient_name, p.date_of_birth, p.contact_info, " +
        "pr.name AS provider_name, pr.specialty, pr.location " +
        "FROM appointments a " +
        "JOIN patients p ON a.patient_id = p.patient_id " +
        "JOIN providers pr ON a.provider_id = pr.provider_id";

    /**
     * Adds a new appointment to the database.
     *
     * @param apt Appointment object to add
     */
    public static void addAppointment(Appointment apt) {
        String sql = "INSERT INTO appointments (appointment_id, patient_id, provider_id, start_time, end_time, reason, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Patient pat = apt.getPatient();
            Provider pro = apt.getProvider();
            AppointmentStatus status = apt.getStatus();

            stmt.setLong(1, apt.getAppointmentID());
            stmt.setLong(2, pat.getPatientID());
            stmt.setLong(3, pro.getProviderID());
            stmt.setLong(4, apt.getStartDateTime());
            stmt.setLong(5, apt.getEndDateTime());
            stmt.setString(6, apt.getReason());
            stmt.setString(7, status.name());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not add appointment.", e);
        }
    }

    /**
     * Gets every appointment currently stored in the database.
     *
     * @return an ArrayList of all saved appointments
     */
    public static ArrayList<Appointment> getAllAppointments() {
        String sql = "SELECT appointment_id, patient_id, provider_id, start_time, end_time, reason, status FROM appointments ORDER BY appointment_id";
        ArrayList<Appointment> apts = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                apts.add(mapAppointment(rs));
            }

            return apts;
        } catch (SQLException e) {
            throw new RuntimeException("Could not list appointments.", e);
        }
    }


    /**
     * Updates the status of an existing appointment.
     *
     * @param appointmentId the ID of the appointment to update
     * @param status the new appointment status
     */
    public static void updateAppointmentStatus(long appointmentId, AppointmentStatus status) {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setLong(2, appointmentId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Appointment not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not update appointment status.", e);
        }
    }

    /**
     * Changes the start and end time for an existing appointment.
     *
     * @param appointmentId the ID of the appointment to reschedule
     * @param newStartTime the new start time
     * @param newEndTime the new end time
     */
    public static void rescheduleAppointment(long appointmentId, long newStartTime, long newEndTime) {
        String sql = "UPDATE appointments SET start_time = ?, end_time = ? WHERE appointment_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, newStartTime);
            stmt.setLong(2, newEndTime);
            stmt.setLong(3, appointmentId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Appointment not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not reschedule appointment.", e);
        }
    }

    /**
     * Deletes an appointment from the database.
     *
     * @param appointmentID the ID of the patient to delete
     */
    public static void deleteAppointment(long appointmentID) {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, appointmentID);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Appointment not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete appointment.", e);
        }
    }

    /**
     * Converts the current database row into an Appointment object.
     *
     * @param rs the result set positioned on an appointment row
     * @return the mapped Appointment object
     * @throws SQLException if a column cannot be read from the result set
     */
    private static Appointment mapAppointment(ResultSet rs) throws SQLException {
        Patient patient = PatientDAO.getPatientByID(rs.getLong("patient_id"));
        Provider provider = ProviderDAO.getProviderByID(rs.getLong("provider_id"));

        return new Appointment(
                rs.getLong("appointment_id"),
                patient,
                provider,
                rs.getLong("start_time"),
                rs.getLong("end_time"),
                AppointmentStatus.valueOf(rs.getString("status")),
                rs.getString("reason")
        );
    }
}
