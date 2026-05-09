package oop.dao;

import oop.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all database operations for Appointment objects.
 *
 * <p>This class is the DAO for the appointments table. It also joins the
 * patients and providers tables so appointment rows can be mapped back into full
 * Appointment objects.</p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class AppointmentDAO {
    private static final long APPOINTMENT_ID_BASE = 700000000L;

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
     * @param patient the patient scheduled for the appointment
     * @param provider the provider assigned to the appointment
     * @param startTime the appointment start time
     * @param endTime the appointment end time
     * @param reason the reason for the appointment
     * @return the Appointment object that was saved to the database
     */
    public Appointment addAppointment(Patient patient, Provider provider, long startTime, long endTime, String reason) {
        long id = getNextAppointmentId();
        AppointmentStatus status = AppointmentStatus.SCHEDULED;

        String sql =
            "INSERT INTO appointments (appointment_id, patient_id, provider_id, start_time, end_time, reason, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setLong(2, patient.getPatientID());
            stmt.setLong(3, provider.getProviderID());
            stmt.setLong(4, startTime);
            stmt.setLong(5, endTime);
            stmt.setString(6, reason);
            stmt.setString(7, status.name());
            stmt.executeUpdate();

            return new Appointment(patient, provider, startTime, endTime, reason);
        } catch (SQLException e) {
            throw new RuntimeException("Could not add appointment.", e);
        }
    }

    /**
     * Finds an appointment using its ID number.
     *
     * @param appointmentId the ID of the appointment to search for
     * @return an Optional containing the appointment if found, or empty otherwise
     */
    public Optional<Appointment> getAppointmentById(long appointmentId) {
        String sql = BASE_SELECT + " WHERE a.appointment_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, appointmentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapAppointment(rs));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Could not get appointment.", e);
        }
    }

    /**
     * Gets every appointment currently stored in the database.
     *
     * @return a list of all saved appointments
     */
    public List<Appointment> getAllAppointments() {
        return queryAppointments(BASE_SELECT + " ORDER BY a.start_time", null);
    }

    /**
     * Gets every appointment for a specific patient.
     *
     * @param patientId the patient ID to filter by
     * @return a list of appointments for the selected patient
     */
    public List<Appointment> getAppointmentsByPatient(long patientId) {
        return queryAppointments(BASE_SELECT + " WHERE a.patient_id = ? ORDER BY a.start_time", patientId);
    }

    /**
     * Gets every appointment for a specific provider.
     *
     * @param providerId the provider ID to filter by
     * @return a list of appointments for the selected provider
     */
    public List<Appointment> getAppointmentsByProvider(long providerId) {
        return queryAppointments(BASE_SELECT + " WHERE a.provider_id = ? ORDER BY a.start_time", providerId);
    }

    /**
     * Gets appointments that are fully inside a date range.
     *
     * @param startTime the beginning of the search range
     * @param endTime the end of the search range
     * @return a list of appointments inside the selected range
     */
    public List<Appointment> getAppointmentsByDateRange(long startTime, long endTime) {
        String sql = BASE_SELECT + " WHERE a.start_time >= ? AND a.end_time <= ? ORDER BY a.start_time";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, startTime);
            stmt.setLong(2, endTime);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }

            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Could not get appointments by date range.", e);
        }
    }

    /**
     * Gets every appointment with a specific status.
     *
     * @param status the appointment status to filter by
     * @return a list of appointments with the selected status
     */
    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        String sql = BASE_SELECT + " WHERE a.status = ? ORDER BY a.start_time";
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }

            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Could not get appointments by status.", e);
        }
    }

    /**
     * Updates the status of an existing appointment.
     *
     * @param appointmentId the ID of the appointment to update
     * @param status the new appointment status
     */
    public void updateAppointmentStatus(long appointmentId, AppointmentStatus status) {
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
    public void rescheduleAppointment(long appointmentId, long newStartTime, long newEndTime) {
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
     * Checks whether a provider already has an overlapping appointment.
     *
     * @param providerId the provider to check
     * @param startTime the proposed start time
     * @param endTime the proposed end time
     * @param ignoredAppointmentId an appointment ID to ignore, or -1 if none
     * @return true if an overlap exists, false otherwise
     */
    public boolean providerHasOverlap(long providerId, long startTime, long endTime, long ignoredAppointmentId) {
        String sql =
            "SELECT COUNT(*) AS total " +
            "FROM appointments " +
            "WHERE provider_id = ? AND appointment_id != ? AND status != 'CANCELLED' AND start_time < ? AND end_time > ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, providerId);
            stmt.setLong(2, ignoredAppointmentId);
            stmt.setLong(3, endTime);
            stmt.setLong(4, startTime);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not check provider appointment overlap.", e);
        }
    }

    /**
     * Runs an appointment query that optionally takes one ID parameter.
     *
     * @param sql the SQL query to run
     * @param id the optional ID parameter, or null if no parameter is needed
     * @return a list of appointments returned by the query
     */
    private List<Appointment> queryAppointments(String sql, Long id) {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (id != null) {
                stmt.setLong(1, id);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(mapAppointment(rs));
                }
            }

            return appointments;
        } catch (SQLException e) {
            throw new RuntimeException("Could not query appointments.", e);
        }
    }

    /**
     * Creates the next appointment ID using the largest ID currently stored.
     *
     * @return the next available appointment ID
     */
    private long getNextAppointmentId() {
        String sql = "SELECT COALESCE(MAX(appointment_id), ?) + 1 AS next_id FROM appointments";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, APPOINTMENT_ID_BASE);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("next_id");
                }
            }

            return APPOINTMENT_ID_BASE + 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create next appointment ID.", e);
        }
    }

    /**
     * Converts the current database row into an Appointment object.
     *
     * @param rs the result set positioned on an appointment row
     * @return the mapped Appointment object
     * @throws SQLException if a column cannot be read from the result set
     */
    private Appointment mapAppointment(ResultSet rs) throws SQLException {
        Patient patient = new Patient(
                rs.getString("patient_name"),
                rs.getString("date_of_birth"),
                rs.getString("contact_info")
        );

        Provider provider = new Provider(
                rs.getString("provider_name"),
                rs.getString("specialty"),
                rs.getString("location")
        );

        return new Appointment(
                patient,
                provider,
                rs.getLong("start_time"),
                rs.getLong("end_time"),
                //AppointmentStatus.valueOf(rs.getString("status")),
                rs.getString("reason")
        );
    }
}
