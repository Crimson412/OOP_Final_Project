package oop.appointment_scheduling_system;

/**
* Test class for appointment scheduling system
*
* @author Rheggeth M.
* @version Final Project, Scheduling System
* @bugs None
*/
public class Test {
    public static void main(String args[]) {
        AppointmentManager manager = new AppointmentManager();
        Patient patient = manager.addPatient();
        Provider provider = manager.addProvider();
        Appointment appointment = manager.scheduleAppointment(patient, provider);
        System.out.println(appointment);
        appointment = manager.rescheduleAppointment(appointment);
        System.out.println(appointment);
        manager.updateAppointmentStatus(appointment);
        System.out.println(appointment);

        Patient pat2 = manager.addPatient();
        Provider prov2 = manager.addProvider();
        Appointment apt2 = manager.scheduleAppointment(pat2, prov2);

        System.out.println("\nAppointments By " + prov2 + ":");
        manager.getAppointmentsByProvider(prov2);

        System.out.println("\nAppointments By " + patient + ":");
        manager.getAppointmentsByPatient(patient);

        System.out.println("\nAppointments By Date Range [0, 5]:");
        manager.getAppointmentsByDateRange(0, 5);

        System.out.println("\nAppointments By Status COMPLETED:");
        manager.getAppointmentsByStatus(AppointmentStatus.COMPLETED);
    }
}   
