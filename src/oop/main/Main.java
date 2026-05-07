package oop.main;

//import oop.dao.*;
import oop.service.AppointmentManager;
import oop.model.*;
import java.util.Scanner;

/**
 * Main class to run appointment scheduling system.
 *
 * @author Rheggeth M.
 * @version Final Project, main
 * @bugs None
 */
public class Main {
    public static void main(String args[]) {   
        Scanner in = new Scanner(System.in);

        // TODO Start by grabbing data from database via dao classes

        // Create singular appointment manager instance and add in database values
        AppointmentManager manager = new AppointmentManager();

        // TODO add stuff

        System.out.println("Database finished loading");

        // Enter logic loop
        char input = 'a';
        boolean loop = true;
        Patient pat;
        Provider pro;
        Appointment apt;

        while (input != 'Q') {
            System.out.print("\nWhich action would you like to perform?\nCreate, Read, Update, or Delete [C, R, U, D, or Q to Quit]\n> ");
            input = in.next().charAt(0);
            input = Character.toUpperCase(input);
            loop = true;
            switch (input) {
                case 'C':
                    // create
                    while (loop) {
                        System.out.print("\nWhat would you like to create:\n1. Patient\n2. Provider\n3. Appointment\n4. Exit create menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // create Patient
                                manager.addPatient();
                                break;
                            case '2':
                                // create Provider
                                manager.addProvider();
                                break;
                            case '3':
                                // create Appointment
                                manager.scheduleAppointment();
                                break;
                            case '4':
                                // go back a step
                                System.out.println("Leaving creation menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, 3, or 4");
                        }
                    }
                    break;
                case 'R':
                    // read
                    while (loop) {
                        System.out.print("\nWhat would you like to sort by:\n1. List all Patients\n2. List all Providers\n" +
                            "3. List all Appointments\n4. List Appointments by Patient\n5. List Appointments by Provider\n" +
                            "6. List Appointment by date range\n7. List Appointment by status\n8. Exit read menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // list all patients
                                manager.listPatients();
                                break;
                            case '2':
                                // list all providers
                                manager.listProviders();
                                break;
                            case '3':
                                // list all appointments
                                manager.listAppointments();
                                break;
                            case '4':
                                // list appointments by patient
                                manager.getAppointmentsByPatient();
                                break;
                            case '5':
                                // list appointments by provider
                                manager.getAppointmentsByProvider();
                                break;
                            case '6':
                                // list appointments by date range
                                manager.getAppointmentsByDateRange();
                                break;
                            case '7':
                                // list appointments by status
                                manager.getAppointmentsByStatus();
                                break;
                            case '8':
                                // go back a step
                                System.out.println("Leaving read menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1-8");
                        }
                    }
                    break;
                case 'U':
                    // update
                    while (loop) {
                        System.out.print("\nWhat would you like to update:\n1. Reschedule an Appointment\n2. Update an Appointment Status" +
                            "\n3. Exit update menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // reschedule appointment
                                manager.rescheduleAppointment();
                                break;
                            case '2':
                                // update appointment status
                                manager.updateAppointmentStatus();
                                break;
                            case '3':
                                // go back a step
                                System.out.println("Leaving update menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, or 3");
                        }
                    }
                    break;
                case 'D':
                    // delete
                    while (loop) {
                        System.out.print("What would you like to delete:\n1. Patient\n2. Provider\n3. Exit delete menu\n> ");
                        input = in.next().charAt(0);
                        switch (input) {
                            case '1':
                                // delete patient
                                manager.deletePatient();
                                break;
                            case '2':
                                // delete provider
                                manager.deleteProvider();
                                break;
                            case '3':
                                // go back a step
                                System.out.println("Leaving delete menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, or 3");
                        }
                    }
                    break;
                case 'Q':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Please only enter a valid character!");
            }
        }
    }
}
