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
 * @bugs deleteAppointment() probably shouldn't exist but due to the foreign keys in the sqlite database
 * it is required so that patients and provider can be deleted.
 */
public class AppointmentManager {
    private ArrayList<Appointment> appoints = new ArrayList<>();
    private ArrayList<Patient> patients     = new ArrayList<>();
    private ArrayList<Provider> providers   = new ArrayList<>();

    /**
     * Creates and adds a patient to our patient array list variable, only letting valid input through.
     *
     * @return returns the new Patient object
     */
    public Patient addPatient() {
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
        return newPatient;
    }

    // Setter for patients array list
    public void setPatientsList(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    /**
     * Method that attempts to get a valid patient using patientID input.
     *
     * @return Patient object that is null if no patient found, or the patient on success
     */
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
     *
     * @return Provider object of the created provider
     */
    public Provider addProvider() {
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
            if (Objects.isNull(specialty) || specialty.trim().isEmpty()) System.out.println("Provider specialty cannot be empty!");
            else break;
        }
        // get the location
        while (true) {
            System.out.print("Enter the providers location: ");
            location = in.nextLine();
            if (Objects.isNull(location) || location.trim().isEmpty()) System.out.println("Provider location cannot be empty!");
            else break;
        }

        // create the provider
        Provider newProvider = new Provider(name, specialty, location);
        providers.add(newProvider);
        System.out.println("Created " + newProvider);
        return newProvider;
    }

    // Setter for providers array list
    public void setProvidersList(ArrayList<Provider> providers) {
        this.providers = providers;
    }

    /**
     * Method that attempts to get a provider given the providerID.
     *
     * return Provider object if provider found, null otherwise
     */
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

    /**
     * Method which enforces the business rule that a provider cannot have two appointments at the same time.
     *
     * @param provider the provider which the business rule is being checked for
     * @param start the proposed start time for this providers appointment
     * @param end the proposed end time for this providers appointment
     * @param apt null if the appointment is being shceduled, else its the appointment being rescheduled
     * @return boolean, true if the appointment can be made/rescheduled, false otherwise
     */
    public boolean verifyProviderAppointments(Provider provider, long start, long end, Appointment apt) {
        ArrayList<Appointment> apts = getAppointmentsByProvider(provider);
        // ensure that rescheduling an appointment doesn't account for the initial appointment
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

    /**
     * Method which gives a valid date range from user input following the business logic.
     *
     * @return long[] array with 2 entries, the first is the start time, and the second is the end
     */
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
            // business rule, end time should not occur before or at the start time
            if (end <= start) System.out.println("Start must occur before end time!");
            else return new long[] {start, end};
        }
    }

    /**
     * Method which gets a valid appointment status from user input.
     *
     * @return AppointmentStatus enum of any of its three types
     */
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
     * Schedules an appointment if good data achieved then adds it to the appointments array list.
     * <p>
     * This enforces the business rule that an appointment cannot be created without a patient
     * and provider being created first. If either getPatient() or getProvider() returns a null
     * value this method will simply return. Otherwise, it then gets a valid date range enforced
     * by getDateRange() and then verifies our provider overlap business rule. If all these rules
     * pass, then the reasong is inputted and the appointment is created.
     * </p>
     *
     * @return Appointment object on success, null otherwise
     */
    public Appointment scheduleAppointment() {
        Scanner in = new Scanner(System.in);
        long min, max;
        Patient patient = getPatient();
        if (patient == null) return null;
        Provider provider = getProvider();
        if (provider == null) return null;

        long[] dateRange = getDateRange();
    
        if (!verifyProviderAppointments(provider, dateRange[0], dateRange[1], null)) return null;

        // get the reason (can be empty):
        //in.nextLine();
        System.out.print("Please enter a reason: ");
        String reason = in.nextLine();

        // Create and add appointment
        Appointment newApt = new Appointment(patient, provider, dateRange[0], dateRange[1], reason);
        appoints.add(newApt);
        System.out.println("Created " + newApt);
        return newApt;
    }

    // Setter for appoints array list
    public void setAppointmentsList(ArrayList<Appointment> appoints) {
        this.appoints = appoints;
    }

    /**
     * Method which attempts to get a valid appointment from user input for appointmentID.
     *
     * @return Appointment if valid appointment found, else returns null.
     */
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

    /**
     * Attempts to reschedule an appointment time from getAppointment() and checks provider business rule.
     *
     * @return Appointment object if succsessfully rescheduled, null otherwise
     */
    public Appointment rescheduleAppointment() {
        Scanner in = new Scanner(System.in);
        Appointment apt = getAppointment();
        if (apt == null) return null;

        long[] dateRange = getDateRange();

        // Ensure the reschedule still holds the provider business rule
        if (!verifyProviderAppointments(apt.getProvider(), dateRange[0], dateRange[1], apt)) return null;

        apt.reschedule(dateRange[0], dateRange[1]);
        System.out.println("Reschedule successful! " + apt);
        return apt;
    }

    /**
     * Method which updates an inputted apointments status, returns early if no appointment found.
     */
    public void updateAppointmentStatus() {
        Appointment apt = getAppointment();
        if (apt == null) return;
        
        AppointmentStatus status = getAppointmentStatus();
        apt.updateStatus(status);
        System.out.println("Status change successful! " + apt);
    }


    /**
     * Method which prints a list of appointments filted by a given patient returns early if invalid patient.
     */
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

    /**
     * Overloaded method which returns an ArrayList of appointments by a specific provider (used to enforce provider rule).
     *
     * @param p Provider instance which is the provider to filter by
     * @return ArrayList<Appointment> list of all the appointments with this provider
     */
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

    /**
     * Overloaded methid which prints appointments by a provider, returns early if invalid provider.
     */
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

    /**
     * Method which prints appointments WITHIN a given time frame (not overlapping).
     */
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

    /**
     *  Method that prints all appointments with a given status.
     */
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

    /**
     * Method that prints all the Patient objects in patients ArrayList.
     */
    public void listPatients() { 
        for (int i = 0; i < patients.size(); i++) System.out.println(patients.get(i));
    }

    /**
     * Method that prints all the Provider objects in providers ArrayList.
     */
    public void listProviders() { 
        for (int i = 0; i < providers.size(); i++) System.out.println(providers.get(i));
    }

    /**
     * Method that prints all the Appointment objects in appoints ArrayList.
     */
    public void listAppointments() { 
        for (int i = 0; i < appoints.size(); i++) System.out.println(appoints.get(i));
    }

    /**
     * Method that deletes a patient, returns early if invalid patient entered.
     *
     * @return Patient object if successfully deleted, null otherwise
     */
    public Patient deletePatient() {
        Patient patient = getPatient();
        if (patient == null) return null;

        patients.remove(patient);
        System.out.println("Removed: " + patient);
        return patient;
    }

    /**
     * Method that deletes a provider, returns early if invalid provider entered.
     *
     * @return Provider object if successful, null otherwise
     */
    public Provider deleteProvider() {
        Provider provider = getProvider();
        if (provider == null) return null;

        providers.remove(provider);
        System.out.println("Removed: " + provider);
        return provider;
    }

    /**
     * Method that deletes an appointment, returns early if invalid appointment entered.
     *
     * @return Appointment object if successful, null otherwise
     */
    public Appointment deleteAppointment() {
        Appointment apt = getAppointment();
        if (apt == null) return null;

        appoints.remove(apt);
        System.out.println("Removed: " + apt);
        return apt;
    }
}
