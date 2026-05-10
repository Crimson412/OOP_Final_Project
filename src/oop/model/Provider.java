package oop.model;

/**
 * Provider class storing their ID, name, specialty, and location.
 *
 * @author Noah W.
 * @version Final Project, model
 * @bugs None
 */
public class Provider {
    private static int numProviders = 0;
    private long providerID = 800000000;
    private String name;
    private String specialty;
    private String location;

    /**
     * Default constructor that directly sets the values for this provider.
     * 
     * @param name String of this providers name
     * @param specialty String describing this providers specialty
     * @param location String describing this providers location (e.g. address)
     */
    public Provider(String name, String specialty, String location) {
        numProviders++;

        // Then we create a unique ID by adding the total number of providers to the default ID - Rheggeth
        providerID += numProviders;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    /**
     * Constructor used when we already know the providers ID, when we load in from sql database.
     *
     * @param id the providerss id
     * @param name the providerss name
     * @param specialty the providers specialty
     * @param location the providers location
     */
    public Provider(long id, String name, String specialty, String location) {
        providerID = id;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and Setters.

    public long getProviderID() { return providerID; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }

    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    /**
     * Method that updates the number of providers given the last ID found in the database.
     *
     * @bugs Hard coding the 800# feels pretty bad but providerID is not static
     */
    public static void updateNumProviders(long lastID) {
        numProviders = (int)(lastID - 800000000);
    }

    /**
     * Returns a string representation of this provider.
     *
     * @return a string describing this provider
     */
    @Override    
    public String toString() {
        return String.format("Provider [ID: %d, Name: %s, Specialty: %s, Location: %s]", 
                providerID, name, specialty, location);
    }
}
