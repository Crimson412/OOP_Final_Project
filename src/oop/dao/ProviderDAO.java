package oop.dao;

import oop.model.Provider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handles all database operations for Provider objects.
 *
 * <p>
 * This class is the DAO for the providers table. It keeps SQL code separated
 * from the service layer and turns provider rows into Provider objects.
 * </p>
 *
 * @author Luis Franco
 * @version Final Project, DAO
 * @bugs None
 */
public class ProviderDAO {
    /**
     * Adds a new provider to the database.
     *
     * @param provider the provider to be added to the database 
     * */
    public static void addProvider(Provider provider) {
        String sql = "INSERT INTO providers (provider_id, name, specialty, location) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1,   provider.getProviderID());
            stmt.setString(2, provider.getName());
            stmt.setString(3, provider.getSpecialty());
            stmt.setString(4, provider.getLocation());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not add provider.", e);
        }
    }

    /**
     * Gets every provider currently stored in the database.
     *
     * @return an ArrayList of all saved providers
     */
    public static ArrayList<Provider> getAllProviders() {
        String sql = "SELECT provider_id, name, specialty, location FROM providers ORDER BY provider_id";
        ArrayList<Provider> providers = new ArrayList<>();

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
     * Finds a provider using their ID number.
     *
     * @param providerID the ID of the provider to search for
     * @return Provider object containing the provider if found, or null otherwise
     */
    public static Provider getProviderByID(long providerID) {
        String sql = "SELECT provider_id, name, specialty, location FROM providers WHERE provider_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, providerID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapProvider(rs);
                }
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Could not get provider.", e);
        }
    }

    /**
     * Deletes a provider from the database.
     *
     * <p>
     * The database uses foreign keys, so this will fail if the provider still
     * has appointments connected to them.
     * </p>
     *
     * @param providerID the ID of the provider to delete
     */
    public static void deleteProvider(long providerID) {
        String sql = "DELETE FROM providers WHERE provider_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, providerID);
            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new IllegalArgumentException("Provider not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not delete provider. This provider may still have appointments.", e);
        }
    }

    /**
     * Converts the current database row into a Provider object.
     *
     * @param rs the result set positioned on a provider row
     * @return the mapped Provider object
     * @throws SQLException if a column cannot be read from the result set
     */
    private static Provider mapProvider(ResultSet rs) throws SQLException {
        return new Provider(
                rs.getLong("provider_id"),
                rs.getString("name"),
                rs.getString("specialty"),
                rs.getString("location")
        );
    }
}
