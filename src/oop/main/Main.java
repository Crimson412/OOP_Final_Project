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
                        System.out.print("What would you like to create:\n1. Patient\n2. Provider\n3. Appointment\n4. Exit create menu\n> ");
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
                                System.out.print("Enter the patients ID number: ");
                                if (in.hasNextInt()) pat = manager.getPatient(in.nextInt());
                                else {
                                    System.out.println("Patient IDs are a 900 number!\n");
                                    in.next();
                                    break;
                                }
                                System.out.print("Enter the providers ID number: ");
                                if (in.hasNextInt()) pro = manager.getProvider(in.nextInt());
                                else { 
                                    System.out.println("Provider IDs are an 800 number!\n");
                                    in.next();
                                    break;
                                }
                                // create appointment if valid patient and providers are returned
                                if (pat != null && pro != null) manager.scheduleAppointment(pat, pro);
                                else System.out.println("Invalid Patient/Provider ID entered!\n");
                                break;
                            case '4':
                                // go back a step
                                System.out.println("Leaving creation menu...");
                                loop = false;
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, 3, or 4\n");
                        }
                    }
                    break;
                case 'R':
                    // read
                    while (loop) {
                        System.out.print("What would you like to sort by:\n1. List Appointments by Patient\n2. List Appointments by Provider" +
                                "\n3. List Appointment by date range\n4. List Appointment by status\n5. Exit read menu\n> ");
                        input = in.next().charAt(0);
                        loop = false;
                        switch (input) {
                            case '1':
                                // list appointments by patient
                                break;
                            case '2':
                                // list appointments by provider
                                break;
                            case '3':
                                // list appointments by date range
                                break;
                            case '4':
                                // list appointments by status
                                break;
                            case '5':
                                // go back a step
                                System.out.println("Leaving read menu...");
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1-5");
                                loop = true;
                        }
                    }
                    break;
                case 'U':
                    // update
                    while (loop) {
                        System.out.print("What would you like to update:\n1. Reschedule an Appointment\n2. Update an Appointment Status" +
                            "\n3. Exit update menu\n> ");
                        input = in.next().charAt(0);
                        loop = false;
                        switch (input) {
                            case '1':
                                // reschedule appointment
                                break;
                            case '2':
                                // update appointment status 
                                break;
                            case '3':
                                // go back a step
                                System.out.println("Leaving update menu...");
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, or 3");
                                loop = true;
                        }
                    }
                    break;
                case 'D':
                    // delete
                    while (loop) {
                        System.out.print("What would you like to delete:\n1. Patient\n2. Provider\n3. Exit delete menu\n> ");
                        input = in.next().charAt(0);
                        loop = false;
                        switch (input) {
                            case '1':
                                // delete patient
                                break;
                            case '2':
                                // delete provider
                                break;
                            case '3':
                                // go back a step
                                System.out.println("Leaving delete menu...");
                                break;
                            default:
                                System.out.println("Please only enter one of the numbers 1, 2, or 3");
                                loop = true;
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
