package oop.service;

import oop.model.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Objects;

/**
 * Appointment manager class that handles appointments, patients, and providers using the busidness rules.
 *
 * @author Rheggeth M.
 * @version Final Project, service.
 * @bugs None
 */
public class AppointmentManager {
    private ArrayList<Appointment> appoints  = new ArrayList<>();
    private ArrayList<Patient> patients  = new ArrayList<>();
    private ArrayList<Provider> providers = new ArrayList<>();

    /**
     * Creates and adds a patient to our patient array list variable, only letting valid input through.
     */
    public void addPatient() {
        Scanner in = new Scanner(System.in);
        String name, dob, contact;

        // get the name
        while (true) {
            System.out.println("Enter the patients name:");
            name = in.nextLine();
            if (Objects.isNull(name) || name.trim().isEmpty()) System.out.println("Patient name cannot be empty!");
            else break;
        }
        // get date of birth
        // TODO: could eventaully be updated to a local date object
        while (true) {
            System.out.println("Enter the patients DOB:");
            dob = in.nextLine();
            if (Objects.isNull(dob) || dob.trim().isEmpty()) System.out.println("DOB cannot be empty!");
            else break;
        }
        // get contact info (this can be empty)
        System.out.println("Enter any contact information:");
        contact = in.nextLine();

        // create the patient
        Patient newPatient = new Patient(name, dob, contact);
        patients.add(newPatient);
        System.out.println("Created: " + newPatient + "\n");
    }

    public Patient getPatient(int patientID) {
        for (int i = 0; i < patients.size(); i++) 
            if (patients.get(i).getPatientID() == patientID) return patients.get(i);
        return null;
    }

    /**
     * Creates and adds a provider to our provider array list variable, only letting valid input through.
     */
    public void addProvider() {
        Scanner in = new Scanner(System.in);
        String name, specialty, location;

        // get the name
        while (true) {
            System.out.println("Enter the providers name:");
            name = in.nextLine();
            if (Objects.isNull(name) || name.trim().isEmpty()) System.out.println("Providers name cannot be empty!");
            else break;
        }
        // get the specialty
        while (true) {
            System.out.println("Enter the providers specialty:");
            specialty = in.nextLine();
            if (Objects.isNull(specialty) || specialty.trim().isEmpty()) System.out.println("Patient specialty cannot be empty!");
            else break;
        }
        // get the location
        while (true) {
            System.out.println("Enter the providers location:");
            location = in.nextLine();
            if (Objects.isNull(location) || location.trim().isEmpty()) System.out.println("Patient location cannot be empty!");
            else break;
        }

        // create the provider
        Provider newProvider = new Provider(name, specialty, location);
        providers.add(newProvider);
        System.out.println("Created " + newProvider + "\n");
    }

    public Provider getProvider(int providerID) {
        for (int i = 0; i < providers.size(); i++) 
            if (providers.get(i).getProviderID() == providerID) return providers.get(i);
        return null;
    }

    /**
     * Schedules an appointment, adds it to the appointments array list, and returns it.
     *
     * @param patient Patient object for the patient this appointment is for
     * @param provider Provider object for the provider of this appointment
     * @return Appointment object of the created appointment, null on failure to create appointment
     * @bugs None
     */
    public boolean scheduleAppointment(Patient patient, Provider provider) {
        Scanner in = new Scanner(System.in);
        long start, end, min, max;

        // start and end times...
        // TODO: have some nice interface so users arent entering Unix Timestamps and then convert it after

        // For now thou... send in the timestamps!
        System.out.print("Please enter the start time: ");
        // prob should be a long so our program doesnt break in 2038
        start = in.nextLong();
        System.out.print("Please enter the end time: ");
        end = in.nextLong();

        // verifies provider doesnt overlap
        ArrayList<Appointment> apts = getAppointmentsByProvider(provider);
        for (int i = 0; i < apts.size(); i++){
            min = (apts.get(i)).getStartDateTime();
            max = (apts.get(i)).getEndDateTime();
            if ((min < start && start < max) || (min < end && end < max)) {
                System.out.println("This provder is already scheduled for this time...");
                return false;
            }
        }

        // Finally for the reason:
        in.nextLine();
        System.out.print("Please enter a reason: ");
        String reason = in.nextLine();

        // Create and add appointment
        Appointment newApt = new Appointment(patient, provider, start, end, reason);
        appoints.add(newApt);
        System.out.println("Created " + newApt + "\n");
        return true;
    }

    // Update requirement
    public boolean rescheduleAppointment(Appointment apt) {
        // this will just delete the old one and make a new appointment
        appoints.remove(apt);

        // TODO this creates a new appointment ID. I'd imagine rescheduling an appointment
        // should actually retain the ID i was just being lazy for now
        
        return scheduleAppointment(apt.getPatient(), apt.getProvider());
    }

    // Update requirement
    public void updateAppointmentStatus(Appointment apt){
        // create our scanner object
        Scanner in = new Scanner(System.in);

        while(true){
            System.out.println("Enter the appointment status: [S, C, X]");
            char status = in.next().charAt(0);
            switch(status){
                case 'S':
                    apt.updateStatus(AppointmentStatus.SCHEDULED);
                    return;
                case 'C':
                    apt.updateStatus(AppointmentStatus.COMPLETED);
                    return;
                case 'X':
                    apt.updateStatus(AppointmentStatus.CANCELLED);
                    return;
                default:
                    System.out.println("Only enter 'S', 'C', or 'X' corresponding to Scheduled, Completed, and Cancelled");
            }
        }
    }

    public void updateAppointmentStatus(Appointment apt, AppointmentStatus status){ apt.updateStatus(status); }


    // The next four return an array list of appointments by specific parameters
    // Falls under Read (Query) requirement
    
    public void getAppointmentsByPatient(Patient p){
        long id = p.getPatientID();
        Appointment apt;
        Patient patient;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            patient = apt.getPatient();

            // for now we will just print the appointments that match
            if (id == patient.getPatientID())
                System.out.println(apt);
        }
    }

    public ArrayList<Appointment> getAppointmentsByProvider(Provider p){
        long id = p.getProviderID();
        ArrayList<Appointment> aptByProvider = new ArrayList<>();
        Appointment apt;
        Provider provider;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            provider = apt.getProvider();

            // for this we will print AND return the modified array (useful for our business stuff)
            if (id == provider.getProviderID()){
                aptByProvider.add(apt);
                System.out.println(apt);
            }
        }
        return aptByProvider;
    }

    // only appointments that start and end within the range given
    public void getAppointmentsByDateRange(long min, long max){
        Appointment apt;
        long start, end;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            start = apt.getStartDateTime();
            end = apt.getEndDateTime();

            // for now we will just print the appointments that match
            if (min <= start && end <= max)
                System.out.println(apt);
        }
    }

    public void getAppointmentsByStatus(AppointmentStatus status){
        Appointment apt;
        AppointmentStatus aptStatus;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            aptStatus = apt.getStatus();

            // for now we will just print the appointments that match
            // turns out using == is actually preferred for enums... neat
            if (status == aptStatus)
                System.out.println(apt);
        }
    }

    // TODO write code for deleting patients and providers whilst keeping rules intact
}
