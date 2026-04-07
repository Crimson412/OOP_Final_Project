package oop.appointment_scheduling_system;

import java.util.Objects;

/**
 * An appointment containing all the necessary information between patient and provider
 *
 * @author Luis Franco
 * @version Final Project, Scheduling System
 * @bugs None 
 */
public class Appointment 
{
    private static int numAppointments = 0;
    private long appointmentId = 700000000;
    private Patient patient;
    private Provider provider;
    private long startDateTime;
    private long endDateTime;
    private AppointmentStatus status;
    private String reason;

    /**
     * Creates a new Appointment
     *
     * @param appointmentId the appointment ID
     * @param patient the patient for this appointment
     * @param provider the provider for this appointment
     * @param startDateTime the appointment start time 
     * @param endDateTime the appointment end time 
     * @param status the appointment status
     * @param reason the reason for the visit
     * @throws IllegalArgumentException if any required information is invalid
     */

    public Appointment(Patient patient, Provider provider, long start, long end, String reason) {
        numAppointments++;
        appointmentId += numAppointments;

        validatePatient(patient);
        validateProvider(provider);
        validateTimes(start, end);
        //validateStatus(status); -dont need to input status, appointment is SCHEDULED if no errors occur
        //validateReason(reason); -RG

        this.patient = patient;
        this.provider = provider;
        startDateTime = start;
        endDateTime = end;
        status = AppointmentStatus.SCHEDULED;
        this.reason = reason;
    }

    // Getters and Setters -Rheggeth
    public long getAppointmentId() { return appointmentId; }

    public Patient getPatient() { return patient; }

    public void setPatient(Patient patient) {
        validatePatient(patient);
        this.patient = patient;
    }

    public Provider getProvider() { return provider; }

    public void setProvider(Provider provider) {
        validateProvider(provider);
        this.provider = provider;
    }

    public long getStartDateTime() { return startDateTime; }

    public void setStartDateTime(long startDateTime) {
        validateTimes(startDateTime, this.endDateTime);
        this.startDateTime = startDateTime;
    }

    public long getEndDateTime() { return endDateTime; }

    public void setEndDateTime(long endDateTime) {
        validateTimes(this.startDateTime, endDateTime);
        this.endDateTime = endDateTime;
    }

    public AppointmentStatus getStatus() { return status; }

    public String getReason() { return reason; }

    public void setReason(String reason) {
        //validateReason(reason); -RG
        this.reason = reason;
    }

    /**
     * Updates the status of this appointment
     * @param newStatus the new appointment status
     */
    // This is just setStatus again... maybe we should remove it or rethink our Setters - Rheggeth
    // Update I removed setStatus
    public void updateStatus(AppointmentStatus newStatus) {
        validateStatus(newStatus);
        status = newStatus;
    }

    /**
     * Reschedules this appointment to a new time
     * @param newStartDateTime the new start time
     * @param newEndDateTime the new end time
     */
    public void reschedule(long newStartDateTime, long newEndDateTime) {
        validateTimes(newStartDateTime, newEndDateTime);
        startDateTime = newStartDateTime;
        endDateTime = newEndDateTime;
    }

    /**
     * Returns a string representation of this appointment
     * @return a string describing this appointment
     */
    @Override
    public String toString() {
        return "Appointment{appointmentId=" + appointmentId + ", patient=" + patient.getName() + ", provider=" 
                + provider.getName() + ", startDateTime=" + startDateTime + ", endDateTime=" + endDateTime 
                + ", status=" + status + ", reason='" + reason + "'}";
    }

    //The following are validations 

    private void validatePatient(Patient patient) {
        if (Objects.isNull(patient))
            throw new IllegalArgumentException("Patient cannot be null");
    }


    private void validateProvider(Provider provider) {
        if (Objects.isNull(provider))
            throw new IllegalArgumentException("Provider cannot be null");
    }


    private void validateTimes(long startDateTime, long endDateTime) {
        if (startDateTime >= endDateTime)
            throw new IllegalArgumentException("Appointment start time must be before end time");
    }


    private void validateStatus(AppointmentStatus status) {
        if (Objects.isNull(status))
            throw new IllegalArgumentException("Status cannot be null");
    }

    // this one we probably don't need - Rheggeth
    private void validateReason(String reason) {
        if (Objects.isNull(reason) || reason.trim().isEmpty())
            throw new IllegalArgumentException("Appointment reason cannot be null/blank");
    }
}
