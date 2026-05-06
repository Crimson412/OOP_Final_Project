package oop.model;

import java.util.Objects;

/**
 * An appointment containing all the necessary information between patient and provider.
 *
 * @author Luis Franco
 * @version Final Project, model
 * @bugs None 
 */
public class Appointment {
    private static int numAppointments = 0;
    private long appointmentId = 700000000;
    private Patient patient;
    private Provider provider;
    private long startDateTime;
    private long endDateTime;
    private AppointmentStatus status;
    private String reason;

    /**
     * Creates a new Appointment.
     *
     * @param appointmentId the appointment ID
     * @param patient the patient for this appointment
     * @param provider the provider for this appointment
     * @param startDateTime the appointment start time 
     * @param endDateTime the appointment end time 
     * @param status the appointment status
     * @param reason the reason for the visit
     */
    public Appointment(Patient patient, Provider provider, long start, long end, String reason) {
        numAppointments++;
        appointmentId += numAppointments;
        this.patient = patient;
        this.provider = provider;
        startDateTime = start;
        endDateTime = end;
        status = AppointmentStatus.SCHEDULED;
        this.reason = reason;
    }

    // Getters and Setters.

    public long getAppointmentId() { return appointmentId; }

    public Patient getPatient() { return patient; }

    // might remove this
    public void setPatient(Patient patient) { this.patient = patient; }

    public Provider getProvider() { return provider; }

    // might remove this
    public void setProvider(Provider provider) { this.provider = provider; }

    public long getStartDateTime() { return startDateTime; }

    // might remove this
    public void setStartDateTime(long startDateTime) { this.startDateTime = startDateTime; }

    public long getEndDateTime() { return endDateTime; }

    // might remove this
    public void setEndDateTime(long endDateTime) { this.endDateTime = endDateTime; }

    public AppointmentStatus getStatus() { return status; }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    /**
     * Updates the status of this appointment.
     * @param newStatus the new appointment status
     */
    public void updateStatus(AppointmentStatus newStatus) {
        status = newStatus;
    }

    /**
     * Reschedules this appointment to a new time.
     * @param newStartDateTime the new start time
     * @param newEndDateTime the new end time
     */
    public void reschedule(long newStartDateTime, long newEndDateTime) {
        startDateTime = newStartDateTime;
        endDateTime = newEndDateTime;
    }

    /**
     * Returns a string representation of this appointment.
     * @return a string describing this appointment
     */
    @Override
    public String toString() {
        return "Appointment{appointmentId=" + appointmentId + ", patient=" + patient.getName() + ", provider=" 
                + provider.getName() + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime 
                + ", status=" + status + ", reason='" + reason + "'}";
    }
}
