package oop.dao;

import oop.model.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles all database operations for Provider objects.
 *
 * <p>This class is the DAO for the providers table. It keeps SQL code separated
 * from the service layer and turns provider rows into Provider objects.</p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class ProviderDAO {
    private static final long PROVIDER_ID_BASE = 800000000L;

    /**
     * Adds a new provider to the database.
     *
     * @param name the provider's name
     * @param specialty the provider's medical specialty
     * @param location the provider's office or location
     * @return the Provider object that was saved to the database
     */
    public Provider addProvider(String name, String specialty, String location) {
        long id = getNextProviderId();
        String sql = "INSERT INTO providers (provider_id, name, specialty, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.setString(2, name);
            stmt.setString(3, specialty);
            stmt.setString(4, location);
            stmt.executeUpdate();

            return new Provider(name, specialty, location);
        } catch (SQLException e) {
            throw new RuntimeException("Could not add provider.", e);
        }
    }

    /**
     * Finds a provider using their ID number.
     *
     * @param providerId the ID of the provider to search for
     * @return an Optional containing the provider if found, or empty otherwise
     */
    public Optional<Provider> getProviderById(long providerId) {
        String sql = "SELECT provider_id, name, specialty, location FROM providers WHERE provider_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, providerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapProvider(rs));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Could not get provider.", e);
        }
    }

    /**
     * Gets every provider currently stored in the database.
     *
     * @return a list of all saved providers
     */
    public List<Provider> getAllProviders() {
        String sql = "SELECT provider_id, name, specialty, location FROM providers ORDER BY provider_id";
        List<Provider> providers = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                providers.add(mapProvider(rs));
            }

            return providers;
        } catch (SQLException e) {
            throw new RuntimeException("Could not list providers.", e);
        }
    }

    /**
     * Deletes a provider from the database.
     *
     * <p>The database uses foreign keys, so this will fail if the provider still
     * has appointments connected to them.</p>
     *
     * @param providerId the ID of the provider to delete
     */
    public void deleteProvider(long providerId) {
        String sql = "DELETE FROM providers WHERE provider_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, providerId);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Provider not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete provider. This provider may still have appointments.", e);
        }
    }

    /**
     * Checks whether a provider exists in the database.
     *
     * @param providerId the provider ID to check
     * @return true if the provider exists, false otherwise
     */
    public boolean exists(long providerId) {
        return getProviderById(providerId).isPresent();
    }

    /**
     * Creates the next provider ID using the largest ID currently stored.
     *
     * @return the next available provider ID
     */
    private long getNextProviderId() {
        String sql = "SELECT COALESCE(MAX(provider_id), ?) + 1 AS next_id FROM providers";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, PROVIDER_ID_BASE);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("next_id");
                }
            }

            return PROVIDER_ID_BASE + 1;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create next provider ID.", e);
        }
    }

    /**
     * Converts the current database row into a Provider object.
     *
     * @param rs the result set positioned on a provider row
     * @return the mapped Provider object
     * @throws SQLException if a column cannot be read from the result set
     */
    private Provider mapProvider(ResultSet rs) throws SQLException {
        return new Provider(
                rs.getString("name"),
                rs.getString("specialty"),
                rs.getString("location")
        );
    }
}
