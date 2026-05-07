package oop.model;

/**
 * An appointment containing all the necessary information between patient and provider.
 *
 * @author Luis Franco
 * @version Final Project, model
 * @bugs None 
 */
public class Appointment {
    private static int numAppointments = 0;
    private long appointmentID = 700000000;
    private Patient patient;
    private Provider provider;
    private long startDateTime;
    private long endDateTime;
    private AppointmentStatus status;
    private String reason;

    /**
     * Creates a new Appointment, the status for a new appointment is always SCHEDULED.
     *
     * @param patient the patient for this appointment
     * @param provider the provider for this appointment
     * @param start the appointment start time 
     * @param end the appointment end time 
     * @param reason the reason for the visit
     */
    public Appointment(Patient patient, Provider provider, long start, long end, String reason) {
        numAppointments++;
        appointmentID += numAppointments;
        this.patient = patient;
        this.provider = provider;
        startDateTime = start;
        endDateTime = end;
        status = AppointmentStatus.SCHEDULED;
        this.reason = reason;
    }

    // Getters.

    public long getAppointmentID() { return appointmentID; }

    public Patient getPatient() { return patient; }

    public Provider getProvider() { return provider; }

    public long getStartDateTime() { return startDateTime; }

    public long getEndDateTime() { return endDateTime; }

    public AppointmentStatus getStatus() { return status; }

    public String getReason() { return reason; }

    /**
     * Updates the status of this appointment.
     *
     * @param newStatus the new appointment status
     */
    public void updateStatus(AppointmentStatus newStatus) {
        status = newStatus;
    }

    /**
     * Reschedules this appointment to a new time with inputs already checked.
     *
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
        return "Appointment [ID: " + appointmentID + ", Patient: " + patient.getName() + ", Provider: " 
                + provider.getName() + ", Start: " + startDateTime + ", End: " + endDateTime 
                + ", Status: " + status + ", Reason: " + reason + "]";
    }
}
