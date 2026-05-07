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
    private ArrayList<Appointment> appoints = new ArrayList<>();
    private ArrayList<Patient> patients     = new ArrayList<>();
    private ArrayList<Provider> providers   = new ArrayList<>();

    /**
     * Creates and adds a patient to our patient array list variable, only letting valid input through.
     */
    public void addPatient() {
        Scanner in = new Scanner(System.in);
        String name, dob, contact;

        // get the name
        while (true) {
            System.out.print("Enter the patients name: ");
            name = in.nextLine();
            if (Objects.isNull(name) || name.trim().isEmpty()) System.out.println("Patient name cannot be empty!");
            else break;
        }
        // get date of birth
        // TODO: could eventaully be updated to a local date object
        while (true) {
            System.out.print("Enter the patients DOB: ");
            dob = in.nextLine();
            if (Objects.isNull(dob) || dob.trim().isEmpty()) System.out.println("DOB cannot be empty!");
            else break;
        }
        // get contact info (this can be empty)
        System.out.print("Enter any contact information: ");
        contact = in.nextLine();

        // create the patient
        Patient newPatient = new Patient(name, dob, contact);
        patients.add(newPatient);
        System.out.println("Created: " + newPatient);
    }

    public Patient getPatient() {
        Scanner in = new Scanner(System.in);
        int patientID;

        while (true) {
        System.out.print("Enter the patients ID number: ");
            if (in.hasNextInt()) {
                patientID = in.nextInt();
                break;
            } else {
                System.out.println("Patient ID is a 900 number!");
                in.next();
            }
        }
        for (int i = 0; i < patients.size(); i++) 
            if (patients.get(i).getPatientID() == patientID) return patients.get(i);
        System.out.println("PatientID: " + patientID + " not found!");
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
            System.out.print("Enter the providers name: ");
            name = in.nextLine();
            if (Objects.isNull(name) || name.trim().isEmpty()) System.out.println("Providers name cannot be empty!");
            else break;
        }
        // get the specialty
        while (true) {
            System.out.print("Enter the providers specialty: ");
            specialty = in.nextLine();
            if (Objects.isNull(specialty) || specialty.trim().isEmpty()) System.out.println("Patient specialty cannot be empty!");
            else break;
        }
        // get the location
        while (true) {
            System.out.print("Enter the providers location: ");
            location = in.nextLine();
            if (Objects.isNull(location) || location.trim().isEmpty()) System.out.println("Patient location cannot be empty!");
            else break;
        }

        // create the provider
        Provider newProvider = new Provider(name, specialty, location);
        providers.add(newProvider);
        System.out.println("Created " + newProvider);
    }

    public Provider getProvider() {
        Scanner in = new Scanner(System.in);
        int providerID;

        while (true) {
        System.out.print("Enter the providers ID number: ");
            if (in.hasNextInt()) {
                providerID = in.nextInt();
                break;
            } else {
                System.out.println("Provider ID is an 800 number!");
                in.next();
            }
        }
        for (int i = 0; i < providers.size(); i++) 
            if (providers.get(i).getProviderID() == providerID) return providers.get(i);
        System.out.println("ProviderID: " + providerID + " not found!");
        return null;
    }

    public boolean verifyProviderAppointments(Provider provider, long start, long end, Appointment apt) {
        ArrayList<Appointment> apts = getAppointmentsByProvider(provider);
        apts.remove(apt);
        for (int i = 0; i < apts.size(); i++) {
            long min = (apts.get(i)).getStartDateTime();
            long max = (apts.get(i)).getEndDateTime();
            if ((min <= start && start <= max) || (min <= end && end <= max)) {
                System.out.println("This provder is already scheduled for this time...");
                return false;
            }
        }
        return true;
    }

    public long[] getDateRange() {
        Scanner in = new Scanner(System.in);
        long start, end;

        while (true) {
            while (true) {
                System.out.print("Please enter the start time: ");
                if (in.hasNextInt()) {
                    start = in.nextLong();
                    break;
                } else {
                    System.out.println("Only enter an integer value!");
                    in.next();
                }
            } while (true) {
                System.out.print("Please enter the end time: ");
                if (in.hasNextInt()) {
                    end = in.nextLong();
                    break;
                } else {
                    System.out.println("Only enter an integer value!");
                    in.next();
                }
            }
            if (end <= start) System.out.println("Start must occur before end time!");
            else return new long[] {start, end};
        }
    }

    public AppointmentStatus getAppointmentStatus() {
        Scanner in = new Scanner(System.in);
 
        while(true) {
            System.out.println("Enter the appointment status: [S, C, X]");
            char status = in.next().charAt(0);
            status = Character.toUpperCase(status);
            switch(status) {
                case 'S':
                    return AppointmentStatus.SCHEDULED;
                case 'C':
                    return AppointmentStatus.COMPLETED;
                case 'X':
                    return AppointmentStatus.CANCELLED;
                default:
                    System.out.println("Only enter 'S', 'C', or 'X' corresponding to Scheduled, Completed, and Cancelled");
            }
        }
    }


    /**
     * Schedules an appointment, adds it to the appointments array list, and returns it.
     *
     * @param patient Patient object for the patient this appointment is for
     * @param provider Provider object for the provider of this appointment
     * @return Appointment object of the created appointment, null on failure to create appointment
     * @bugs None
     */
    public void scheduleAppointment() {
        Scanner in = new Scanner(System.in);
        long min, max;
        Patient patient = getPatient();
        if (patient == null) return;
        Provider provider = getProvider();
        if (provider == null) return;

        long[] dateRange = getDateRange();
    
        if (!verifyProviderAppointments(provider, dateRange[0], dateRange[1], null)) return;

        // get the reason (can be empty):
        //in.nextLine();
        System.out.print("Please enter a reason: ");
        String reason = in.nextLine();

        // Create and add appointment
        Appointment newApt = new Appointment(patient, provider, dateRange[0], dateRange[1], reason);
        appoints.add(newApt);
        System.out.println("Created " + newApt);
    }

    public Appointment getAppointment() {
        Scanner in = new Scanner(System.in);
        long appointmentID;

        System.out.print("Enter the Appointments ID number: ");
        if (in.hasNextInt()) appointmentID = in.nextInt();
        else {
            System.out.println("Appointment ID is a 700 number!");
            return null;
        }

        for (int i = 0; i < appoints.size(); i++) 
            if (appoints.get(i).getAppointmentID() == appointmentID) return appoints.get(i);
        System.out.println("AppointmentID: " + appointmentID + " not found!");
        return null;
    }

    // Update requirement
    public void rescheduleAppointment() {
        Scanner in = new Scanner(System.in);
        Appointment apt = getAppointment();
        if (apt == null) return;

        long[] dateRange = getDateRange();

        if (!verifyProviderAppointments(apt.getProvider(), dateRange[0], dateRange[1], apt)) return;

        apt.reschedule(dateRange[0], dateRange[1]);
        System.out.println("Reschedule successful! " + apt);
    }

    // Update requirement
    public void updateAppointmentStatus() {
        Appointment apt = getAppointment();
        if (apt == null) return;
        
        AppointmentStatus status = getAppointmentStatus();
        apt.updateStatus(status);
        System.out.println("Status change successful! " + apt);
    }


    
    public void getAppointmentsByPatient() {
        Patient p = getPatient();

        if (p == null) return;

        long id = p.getPatientID();
        Appointment apt;
        Patient patient;

        for (int i = 0; i < appoints.size(); i++) {
            apt = appoints.get(i);
            patient = apt.getPatient();
            if (id == patient.getPatientID()) System.out.println(apt);
        }
    }

    public ArrayList<Appointment> getAppointmentsByProvider(Provider p) {
        long id = p.getProviderID();
        ArrayList<Appointment> aptByProvider = new ArrayList<>();
        Appointment apt;
        Provider provider;
        for (int i = 0; i < appoints.size(); i++) {
            apt = appoints.get(i);
            provider = apt.getProvider();
            if (id == provider.getProviderID()) aptByProvider.add(apt);
        }
        return aptByProvider;
    }

    public void getAppointmentsByProvider() {
        Provider p = getProvider();
        if (p == null) return;
        long id = p.getProviderID();
        Appointment apt;
        Provider provider;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            provider = apt.getProvider();
            if (id == provider.getProviderID()) System.out.println(apt);
        }
    }

    // only appointments that start and end within the range given
    public void getAppointmentsByDateRange() {
        long[] dateRange = getDateRange();
        Appointment apt;
        long start, end;

        for (int i = 0; i < appoints.size(); i++) {
            apt = appoints.get(i);
            start = apt.getStartDateTime();
            end = apt.getEndDateTime();
            if (dateRange[0] <= start && end <= dateRange[1]) System.out.println(apt);
        }
    }

    public void getAppointmentsByStatus() {
        AppointmentStatus status = getAppointmentStatus();
        Appointment apt;
        AppointmentStatus aptStatus;
        for (int i = 0; i < appoints.size(); i++) {
            apt = appoints.get(i);
            aptStatus = apt.getStatus();
            if (status == aptStatus) System.out.println(apt);
        }
    }

    public void listPatients() { 
        for (int i = 0; i < patients.size(); i++) System.out.println(patients.get(i));
    }

    public void listProviders() { 
        for (int i = 0; i < providers.size(); i++) System.out.println(providers.get(i));
    }

    public void listAppointments() { 
        for (int i = 0; i < appoints.size(); i++) System.out.println(appoints.get(i));
    }

    public void deletePatient() {
        Patient patient = getPatient();
        if (patient == null) return;

        patients.remove(patient);
        System.out.println("Removed: " + patient);
    }

    public void deleteProvider() {
        Provider provider = getProvider();
        if (provider == null) return;

        providers.remove(provider);
        System.out.println("Removed: " + provider);
    }
}
