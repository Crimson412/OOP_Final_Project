package oop.appointment_scheduling_system;

/**
* Provider class storing their ID, name, specialty, and location
*
* @author Noah W.
* @version Final Project, Scheduling System
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

    // Getters and Setters
    public String getProviderId() { return providerId; }
    // No setter for provider ID - Rheggeth

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return String.format("Provider [ID: %d, Name: %s, Specialty: %s, Location: %s]", 
                providerId, name, specialty, location);
    }
}
