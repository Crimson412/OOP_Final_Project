package oop.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the SQLite database connection and creates the required tables.
 *
 * <p>This class keeps the database setup in one place so the main program and
 * service layer do not need to directly create tables or manage database
 * connection settings.</p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class Database {
    private static final String DB_URL = "jdbc:sqlite:src/oop/dao/clinic.db";

    /**
     * Opens a connection to the SQLite database and enables foreign keys.
     *
     * <p>
     * SQLite requires foreign key enforcement to be turned on for each new
     * connection, so this method runs the PRAGMA command every time a connection
     * is created.
     * </p>
     *
     * @return an active database connection
     * @throws SQLException if the database connection cannot be opened
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;
    }

    /**
     * Creates the patients, providers, and appointments tables if needed.
     *
     * <p>
     * This method is called when the program starts. The tables use primary
     * keys, foreign keys, and check constraints so the database matches the
     * project requirements.
     * </p>
     */
    public static void initializeDatabase() {
        String patientsTable =
            "CREATE TABLE IF NOT EXISTS patients (patient_id INTEGER PRIMARY KEY, name TEXT NOT NULL, date_of_birth TEXT NOT NULL, contact_info TEXT)";

        String providersTable =
            "CREATE TABLE IF NOT EXISTS providers (provider_id INTEGER PRIMARY KEY, name TEXT NOT NULL, specialty TEXT NOT NULL, location TEXT NOT NULL)";

        String appointmentsTable =
            "CREATE TABLE IF NOT EXISTS appointments (appointment_id INTEGER PRIMARY KEY, patient_id INTEGER NOT NULL, provider_id INTEGER NOT NULL, " +
            "start_time INTEGER NOT NULL, end_time INTEGER NOT NULL, reason TEXT, status TEXT NOT NULL, " +
            "FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE RESTRICT, " +
            "FOREIGN KEY (provider_id) REFERENCES providers(provider_id) ON DELETE RESTRICT, "+
            "CHECK (end_time > start_time), CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')))";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            stmt.execute(patientsTable);
            stmt.execute(providersTable);
            stmt.execute(appointmentsTable);

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize SQLite database.", e);
        }
    }
}
