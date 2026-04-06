package oop.appointment_scheduling_system;

/**
* Provider class storing their ID, name, specialty, and location
*
* @author Noah W.
* @version Final Project, Scheduling System
* @bugs None
*/
public class Provider {
    private String providerId;
    private String name;
    private String specialty;
    private String location;

    public Provider(String providerId, String name, String specialty, String location) {
        // Validation
        if (providerId == null || providerId.isEmpty()) throw new IllegalArgumentException("Provider ID cannot be empty.");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Provider name cannot be empty.");

        this.providerId = providerId;
        this.name = name;
        this.specialty = specialty;
        this.location = location;
    }

    // Getters and Setters
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    @Override
    public String toString() {
        return String.format("Provider [ID: %s, Name: %s, Specialty: %s, Location: %s]", 
                providerId, name, specialty, location);
    }
}
