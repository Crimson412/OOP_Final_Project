package oop.appointment_scheduling_system;

// we will use this for storing a list of appointments, patients and providers
// which can be thought of as everything in record
import java.util.ArrayList;
// scanner imported so that input validation can be implemented here and not in main
import java.util.Scanner;

/**
* Appointment manager class that handles appointments, patients, and providers using the busidness rules
*
* @author Rheggeth M.
* @version Final Project, Scheduling System
* @bugs None
*/
public class AppointmentManager {

    // create our three array list instance variables
    ArrayList<Appointment> appoints  = new ArrayList<>();
    ArrayList<Patient> patients  = new ArrayList<>();
    ArrayList<Provider> providers = new ArrayList<>();

    /**
    * Creates and adds a patient to our patient array list variable
    *
    * @return returns the patient created
    * @bugs None
    */
    public Patient addPatient(){
        // create our scanner object
        Scanner in = new Scanner(System.in);

        // get the name
        System.out.println("Enter the patients name:");
        String name = in.nextLine();

        // get date of birth
        // TODO: update this to a localDate object when we implement a GUI to enter it
        System.out.println("Enter the patients DOB:");
        String dob = in.nextLine();

        // finally get contact info
        // I'd imagine a patient could leave this blank
        System.out.println("Enter any contact information:");
        String contact = in.nextLine();

        // create the patient
        Patient newPatient = new Patient(name, dob, contact);
        System.out.println("Patient: " + newPatient.getPatientId() + " has been added");

        // for now we will just add this patient to our patients array list
        patients.add(newPatient);
        // we will also return the newPatient for convenience in our other methods
        return newPatient;
    }

    /**
    * Creates and adds a provider to our provider array list variable
    *
    * @return returns the provider created
    * @bugs None
    */
    public Provider addProvider(){
        // create our scanner object
        Scanner in = new Scanner(System.in);

        // get the name of the provider
        System.out.println("Enter the providers name:");
        String name = in.nextLine();

        // get specialty of provider
        System.out.println("Enter the providers specialty:");
        String specialty = in.nextLine();

        // finally get their location
        System.out.println("Enter the providers location:");
        String location = in.nextLine();

        // create the provider
        Provider newProvider = new Provider(name, specialty, location);
        System.out.println("Provider: " + newProvider.getProviderId() + " has been added");

        // for now we will just add the provider to our providers array list
        providers.add(newProvider);
        // we will also return the newProvider for convenience in our other methods
        return newProvider;
    }

    private Appointment scheduleAppointment(Patient patient, Provider provider, long start, long end, String reason){
        Appointment apt = new Appointment(patient, provider, start, end, reason);
        appoints.add(apt);
        System.out.println("Appointment Scheduled!");
        return apt;
    }


    /**
    * Schedules an appointment, adds it to the appointments array list, and returns it
    *
    * @param Takes in the Patient and Provider
    * @return Returns the created appointment
    * @bugs None
    */
    public Appointment scheduleAppointment(Patient patient, Provider provider){
        // create our scanner object
        Scanner in = new Scanner(System.in);

        // start and end times...
        // TODO: have some nice interface so users arent entering Unix Timestamps and then convert it after

        // For now thou... send in the timestamps!
        System.out.println("Please enter the start time:");
        // prob should be a long so our program doesnt break in 2038
        long start = in.nextLong();
        System.out.println("Please enter the end time:");
        long end = in.nextLong();

        // verifies provider doesnt overlap
        ArrayList<Appointment> apts = getAppointmentsByProvider(provider);
        long min, max;
        for (int i = 0; i < apts.size(); i++){
            min = (apts.get(i)).getStartDateTime();
            max = (apts.get(i)).getEndDateTime();
            if ((min < start && start < max) || (min < end && end < max)){ //invalid
                System.out.println("This provder is already scheduled for this time...");
                return null;
            }
        }

        // Finally for the reason:
        in.nextLine();
        System.out.println("Please enter a reason for the appointment");
        String reason = in.nextLine();
        
        return scheduleAppointment(patient, provider, start, end, reason);
    }

    // Update requirement
    public Appointment rescheduleAppointment(Appointment apt){
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
        long id = p.getPatientId();
        Appointment apt;
        Patient patient;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            patient = apt.getPatient();

            // for now we will just print the appointments that match
            if (id == patient.getPatientId())
                System.out.println(apt);
        }
    }

    public ArrayList<Appointment> getAppointmentsByProvider(Provider p){
        long id = p.getProviderId();
        ArrayList<Appointment> aptByProvider = new ArrayList<>();
        Appointment apt;
        Provider provider;
        for (int i = 0; i < appoints.size(); i++){
            apt = appoints.get(i);
            provider = apt.getProvider();

            // for this we will print AND return the modified array (useful for our business stuff)
            if (id == provider.getProviderId()){
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
