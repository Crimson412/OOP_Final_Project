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
    // ideally this is set to a length that will accomidate all providers for appearance purposes
    // for now we will use a 800 number similar to the one nmt uses - Rheggeth
    private long providerId = 800000000;
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
        // Validation
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Provider name cannot be empty.");

        // Then we create a unique ID by adding the total number of providers to the default ID - Rheggeth
        providerId += numProviders;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and Setters.

    public long getProviderId() { return providerId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }

    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    /**
     * Returns a string representation of this provider.
     * @return a string describing this provider
     */
    @Override    
    public String toString() {
        return String.format("Provider [ID: %d, Name: %s, Specialty: %s, Location: %s]", 
                providerId, name, specialty, location);
    }
}
